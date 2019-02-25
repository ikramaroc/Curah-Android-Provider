package com.curahservice.netset.module.myProfile_tabs;

import android.app.Dialog;
import android.content.DialogInterface;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ServiceAdded;
import com.curahservice.netset.gsonModel.ServiceUpdate;
import com.curahservice.netset.gsonModel.SuggestedServices;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.model.ServicesAdapter;
import com.curahservice.netset.module.addServices.SuggestedRecyclerAdapter;
import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonObject;


import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class MyServicesTab1 extends BaseFragment implements ServicesAdapter.editDeleteClick {

    @BindView(R.id.recycle_item)
    RecyclerView recyclerView;
    @BindView(R.id.editDoc_TV)
    Button editDoc_TV;

    @BindView(R.id.hint)
    TextView hint;

    ArrayList<createAccount.Service> service = new ArrayList<>();
    retrofit2.Call<JsonObject> suggestedServicesApi, addService, editService, deleteService, getServices;
    SuggestedServices suggestedServices;
    Dialog dialog;
    RecyclerView suggestion_services_RV;
    AppCompatEditText service_name_ET, enter_price_ET;
    String serviceType = "C", serviceId = "";
    TextView suggested_services_TV, title;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.service_recycle, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        editDoc_TV.setText("ADD NEW SERVICES");
        if (store.getServiceList(Const.SERVICES) != null && store.getServiceList(Const.SERVICES).size() > 0) {
            service.addAll(store.getServiceList(Const.SERVICES));
        }
        setServicesAdapter(recyclerView, service, true, MyServicesTab1.this,false);
        setHint();
        getServices();
    }


    private void setHint() {
        if (checkHasCustomService()) {
            hint.setVisibility(View.VISIBLE);
            hint.setText("Red color services are waiting for admin approval*");
        } else {
            hint.setVisibility(View.GONE);
        }
    }

    private boolean checkHasCustomService() {
        boolean hasCustom = false;
        for (int i = 0; i < store.getServiceList(Const.SERVICES).size(); i++) {
            if (!store.getServiceList(Const.SERVICES).get(i).isIs_approved()) {
                hasCustom = true;
                break;
            } else {
                hasCustom = false;

            }
        }

        return hasCustom;
    }


    void setAdapter_Suggested(final List<createAccount.Service> suggestedServices) {
        final SuggestedRecyclerAdapter mAdapter = new SuggestedRecyclerAdapter(getActivity(), suggestedServices);
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        suggestion_services_RV.setLayoutManager(mLayoutManager);
        suggestion_services_RV.setAdapter(mAdapter);
        suggestion_services_RV.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        serviceType = "P";
                        serviceId = String.valueOf(suggestedServices.get(position).getServiceId());
                        service_name_ET.setText(suggestedServices.get(position).getName());
                        service_name_ET.setEnabled(false);

                        enter_price_ET.setText(String.valueOf(suggestedServices.get(position).getPrice()));
                        enter_price_ET.setSelection(enter_price_ET.getText().length());
                        enter_price_ET.requestFocus();
                    }
                })
        );
    }

    @OnClick(R.id.editDoc_TV)
    void clickAddNewService() {
        getSuggestedServices();
    }

    private void getSuggestedServices() {
        suggestedServicesApi = apiInterfaceAuth.suggestedServices(String.valueOf(store.getInt(Const.USER_ID)));
        apiHitAndHandle.makeApiCall(suggestedServicesApi, this);
    }

    @Override
    public void onSuccess(retrofit2.Call call, Object object) {
        super.onSuccess(call, object);
        if (call == suggestedServicesApi) {
            Gson gson = new Gson();
            suggestedServices = gson.fromJson(object.toString(), SuggestedServices.class);
            dialogServices(suggestedServices.getServices(), "C", "", "", "", false, -1);
        } else if (call == addService) {
            Gson gson = new Gson();
            ServiceAdded servicesAdded = gson.fromJson(object.toString(), ServiceAdded.class);
            if (servicesAdded.getStatus() == 200) {
                serviceId = "";
                store.setSerVices(Const.SERVICES, servicesAdded.getServices());
                service.clear();
                service.addAll(servicesAdded.getServices());
                servicesAdapter.notifyDataSetChanged();
                setHint();
                try {
                    hideSoftKeyboard(getActivity());
                } catch (Exception e) {
                    e.printStackTrace();
                }
            } else {
                showMessage(getActivity(), "", servicesAdded.getMsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }
        } else if (call == editService) {
            try {
                JSONObject result = new JSONObject(object.toString());
                if (result.getInt(Const.STATUS) == 200) {
                    serviceId = "";
                    hideSoftKeyboard(getActivity());
                    Toast.makeText(getActivity(), result.getString(Const.MESSAGE), Toast.LENGTH_LONG).show();
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        } else if (call == deleteService) {
            Gson gson = new Gson();
            ServiceUpdate serviceUpdate = gson.fromJson(object.toString(), ServiceUpdate.class);
            if (serviceUpdate.getStatus() == 200) {
                serviceId = "";
                Toast.makeText(getActivity(), serviceUpdate.getMessage(), Toast.LENGTH_LONG).show();
                store.setSerVices(Const.SERVICES, serviceUpdate.getServices());
                service.clear();
                service.addAll(serviceUpdate.getServices());
                servicesAdapter.notifyDataSetChanged();
                setHint();
            }
        } else if (call == getServices) {
            Gson gson = new Gson();
            ServiceUpdate serviceUpdate = gson.fromJson(object.toString(), ServiceUpdate.class);
            if (serviceUpdate.getStatus() == 200) {
                store.setSerVices(Const.SERVICES, serviceUpdate.getServices());
                service.clear();
                service.addAll(serviceUpdate.getServices());
                servicesAdapter.notifyDataSetChanged();
                setHint();
            }
        }
    }


    private void dialogServices(List<createAccount.Service> suggestedServices, final String serviceTyp, String servicename, String price, String serviceIId, final boolean edit, final int editPosition) {
        if (dialog == null) {
            serviceType = serviceTyp;
            dialog = new Dialog(getActivity(), R.style.DialogTheme);
            dialog.requestWindowFeature(Window.FEATURE_NO_TITLE);
            dialog.setCancelable(false);
            dialog.setContentView(R.layout.add_service_dialog);
            suggested_services_TV = dialog.findViewById(R.id.suggested_services_TV);
            title = dialog.findViewById(R.id.title);
            service_name_ET = dialog.findViewById(R.id.service_name_ET);
            enter_price_ET = dialog.findViewById(R.id.enter_price_ET);
            editextNoZero(enter_price_ET);
            suggestion_services_RV = dialog.findViewById(R.id.suggestion_services_RV);
            if (suggestedServices != null) {
                suggested_services_TV.setVisibility(View.VISIBLE);
                suggestion_services_RV.setVisibility(View.VISIBLE);
                title.setText("Add Service");
                setAdapter_Suggested(suggestedServices);

            } else {
                title.setText("Edit Service");
                suggested_services_TV.setVisibility(View.INVISIBLE);
                suggestion_services_RV.setVisibility(View.GONE);
                service_name_ET.setText(servicename);
                service_name_ET.setEnabled(false);
                enter_price_ET.requestFocus();
                enter_price_ET.setText(price);
                enter_price_ET.setSelection(enter_price_ET.getText().length());
                enter_price_ET.requestFocus();
            }
            TextView add = (TextView) dialog.findViewById(R.id.submit_TV);
            ImageView cancel = (ImageView) dialog.findViewById(R.id.close);
            add.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (service_name_ET.getText().toString().trim().length() == 0) {
                        service_name_ET.setError("Please add service");
                    } else if (enter_price_ET.getText().toString().trim().length() == 0) {
                        service_name_ET.setError("Please add service price");
                    } else if (enter_price_ET.getText().toString().equals("0")) {
                        enter_price_ET.setError("Please add service price");
                        enter_price_ET.requestFocus();
                    } else {
                        boolean edit1 = edit;
                        if (!edit1) {
                            dialog.dismiss();
                            dialog = null;
                            try {
                                hideSoftKeyboard(getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            AddServices(AddService(service_name_ET.getText().toString().trim(), enter_price_ET.getText().toString().trim(), serviceType, serviceId));
                        } else {
                            dialog.dismiss();
                            dialog = null;
                            try {
                                hideSoftKeyboard(getActivity());
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            service.get(editPosition).setPrice(Integer.parseInt(enter_price_ET.getText().toString().trim()));
                            store.setSerVices(Const.SERVICES, service);
                            servicesAdapter.notifyDataSetChanged();
                            editService(serviceId, enter_price_ET.getText().toString().trim());
                        }
                    }


                }
            });
            cancel.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        hideSoftKeyboard(getActivity());
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    serviceId = "";
                    dialog.dismiss();
                    dialog = null;

                }
            });

            dialog.show();
        }
    }

    private void editService(String serviceId, String price) {
        editService = apiInterfaceAuth.updateUserServices(String.valueOf(store.getInt(Const.USER_ID)), serviceId, price);
        apiHitAndHandle.makeApiCall(editService, this);
    }

    private void getServices() {
        getServices = apiInterfaceAuth.getServices(String.valueOf(store.getInt(Const.USER_ID)));
        apiHitAndHandle.makeApiCall(getServices, this);
    }

    private void deleteService(String serviceId) {
        deleteService = apiInterfaceAuth.deleteService(String.valueOf(store.getInt(Const.USER_ID)), serviceId);
        apiHitAndHandle.makeApiCall(deleteService, this);
    }

    private void AddServices(JSONObject service) {
        addService = apiInterfaceAuth.addService(returnRequestBodyJson(service.toString()));
        apiHitAndHandle.makeApiCall(addService, this);
    }

    private JSONObject AddService(String servicename, String price, String servicetype, String serviceid) {
        JSONObject serviceObject = new JSONObject();
        try {
            serviceObject.put("user_id", String.valueOf(store.getInt(Const.USER_ID)));
            JSONArray services = new JSONArray();
            JSONObject singleservice = new JSONObject();
            singleservice.put("name", servicename);
            singleservice.put("price", price);
            if (servicetype.equals("P")) {
                singleservice.put("id", serviceid);
            } else if (serviceid.equals("")) {
                singleservice.put("id", "C");
            } else {
                singleservice.put("id", serviceid);
            }
            services.put(singleservice);
            serviceObject.put("services", services);

        } catch (JSONException e) {
            e.printStackTrace();
        }
        return serviceObject;
    }


    @Override
    public void edit(int position) {
        if (service.get(position).getType().equals("C")) {
            serviceId = String.valueOf(service.get(position).getServiceId());
            dialogServices(null, "C", service.get(position).getName(), String.valueOf(service.get(position).getPrice()), serviceId, true, position);
        } else if (service.get(position).getType().equals("P")) {
            serviceId = String.valueOf(service.get(position).getServiceId());
            dialogServices(null, "P", service.get(position).getName(), String.valueOf(service.get(position).getPrice()), serviceId, true, position);

        }

    }

    public boolean hasPredefinedService(Collection<createAccount.Service> c) {
        for (createAccount.Service o : c) {
            if (o != null && o.getType().equalsIgnoreCase("P")) {
                return true;
            }
        }
        return false;
    }


    @Override
    public void delete(int position) {
        createAccount.Service serviceRemove = service.get(position);
        service.remove(position);
        if (hasPredefinedService(service)) {
            deleteService(serviceRemove.getServiceId() + "");
        } else {
            service.add(position, serviceRemove);
            showMessage(getActivity(), "", getString(R.string.AtleastOnePredefined), new DialogInterface.OnClickListener() {
                @Override
                public void onClick(DialogInterface dialog, int which) {
                    dialog.dismiss();
                }
            });
        }

    }
}
