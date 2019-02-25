package com.curahservice.netset.module.addServices;

import android.app.Activity;
import android.content.DialogInterface;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.FragmentManager;
import android.support.v7.widget.AppCompatEditText;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.telecom.Call;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.curahservice.netset.R;
import com.curahservice.netset.gsonModel.ServiceAdded;
import com.curahservice.netset.gsonModel.SuggestedServices;
import com.curahservice.netset.gsonModel.createAccount;
import com.curahservice.netset.model.ServicesAdapter;
import com.curahservice.netset.model.ServicesModel;
import com.curahservice.netset.module.AddBankDetailFragment;

import com.curahservice.netset.module.base.BaseFragment;
import com.curahservice.netset.module.base.DrawerActivity;
import com.curahservice.netset.module.base.IndexActivity;

import com.curahservice.netset.util.Const;
import com.curahservice.netset.util.RecyclerItemClickListener;
import com.google.gson.Gson;
import com.google.gson.JsonArray;
import com.google.gson.JsonObject;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class AddServicesFragment extends BaseFragment implements ServicesAdapter.editDeleteClick {

    @BindView(R.id.suggestion_services_RV)
    RecyclerView suggestion_services_RV;
    @BindView(R.id.my_services_RV)
    RecyclerView my_services_RV;
    retrofit2.Call<JsonObject> suggestedServicesApi, addService;

    @BindView(R.id.service_name_ET)
    AppCompatEditText service_name_ET;

    @BindView(R.id.enter_price_ET)
    AppCompatEditText enter_price_ET;


    @BindView(R.id.hint)
            TextView hint;
    boolean editPredefined = false;

    List<createAccount.Service> suggested;


    boolean edit = false;
    int editPosition = -1;
    SuggestedServices suggestedServices;
    SuggestedRecyclerAdapter suggestedAdapter;
    ArrayList<createAccount.Service> service = new ArrayList<>();


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return inflater.inflate(R.layout.add_services, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        ButterKnife.bind(this, view);
        setToolbarTitle();
        editextNoZero(enter_price_ET);
        getSuggestedServices();
        setServicesAdapter(my_services_RV, service, true, AddServicesFragment.this,true);

    }

    private void setToolbarTitle() {
        Activity activity = getActivity();
        if (activity instanceof IndexActivity) {
            IndexActivity myactivity = (IndexActivity) activity;
            myactivity.hideToolbar(View.VISIBLE);
            myactivity.getMy_toolbar().setBackgroundColor(Color.TRANSPARENT);
            myactivity.setTitle(getString(R.string.add_services), true, new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    getFragmentManager().popBackStack();
                }
            }, 0);
            myactivity.skipText(View.VISIBLE, "Done", new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    try {
                        if (AddService()!=null) {
                            if (AddService().getJSONArray("services").length() > 0) {
                                AddServices(AddService());
                            }else {
                                Toast.makeText(getActivity(), "Please add Services", Toast.LENGTH_LONG).show();
                            }
                        }else {
                            showMessage(getActivity(), "", getString(R.string.noPredefined), new DialogInterface.OnClickListener() {
                                @Override
                                public void onClick(DialogInterface dialog, int which) {
                                    dialog.dismiss();
                                }
                            });
                        }
                    } catch (JSONException e) {
                        e.printStackTrace();
                    }

                }
            });
        }
    }

    @OnClick(R.id.submit_TV)
    void submitClick() {
        checkValidation();
    }


    private void resetEditField() {
        editPredefined = false;
        service_name_ET.setText("");
        enter_price_ET.setText("");
        edit = false;
        editPosition = -1;
        service_name_ET.setEnabled(true);
        servicesAdapter.notifyDataSetChanged();
        setHint();
        try {
            hideSoftKeyboard(getActivity());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    private void checkValidation() {
        if (service_name_ET.getText().toString().trim().length() == 0) {
            service_name_ET.setError("Please add service name");
            service_name_ET.requestFocus();
        } else if (enter_price_ET.getText().toString().trim().length() == 0) {
            enter_price_ET.setError("Please add price");
            enter_price_ET.requestFocus();
        } else if (enter_price_ET.getText().toString().equals("0")) {
            enter_price_ET.setError("Please add price");
            enter_price_ET.requestFocus();
        } else {
            createAccount.Service servicesModel;
            if (editPredefined) {
                edit = true;
                if (containsName(service, service_name_ET.getText().toString().trim())) {
                    if (service.get(editPosition).getName().equalsIgnoreCase(service_name_ET.getText().toString().trim())) {
                        servicesModel = new createAccount.Service(service_name_ET.getText().toString().trim(), service.get(editPosition).getServiceId(), Integer.parseInt(enter_price_ET.getText().toString()), "P");
                        service.set(editPosition, servicesModel);
                        resetEditField();
                    } else {
                        checkIfAlreadyAdded();
                    }
                } else {
                    createAccount.Service serviceSingle = suggestedServices.getServices().get(editPosition);
                    serviceSingle.setPrice(Integer.parseInt(enter_price_ET.getText().toString()));
                    service.add(serviceSingle);
                    servicesAdapter.notifyDataSetChanged();
                    setHint();
                    suggestedServices.getServices().remove(editPosition);
                    suggestedAdapter.notifyDataSetChanged();
                    resetEditField();
                }
            } else {
                if (containsName(suggestedServices.getServices(), service_name_ET.getText().toString().trim())) {
                    service_name_ET.setError("Rename service or choose from suggested service list");
                    service_name_ET.requestFocus();
                } else if (containsName(service, service_name_ET.getText().toString().trim())) {
                    checkIfAlreadyAdded();
                } else {
                    servicesModel = new createAccount.Service(service_name_ET.getText().toString().trim(), 0, Integer.parseInt(enter_price_ET.getText().toString()), "C");
                    service.add(servicesModel);
                    resetEditField();
                }


            }

        }
    }

    private void checkIfAlreadyAdded() {
        if (edit) {
            for (int i = 0; i < service.size(); i++) {
                if (service.get(i).getName().equals(service_name_ET.getText().toString().trim())) {
                    service.get(i).setName(service_name_ET.getText().toString());
                    service.get(i).setPrice(Integer.parseInt(enter_price_ET.getText().toString()));
                }
            }
            resetEditField();
        } else {
            service_name_ET.setError("Rename service. or Delete prevoius");
            service_name_ET.requestFocus();
        }
    }

    public boolean containsName(Collection<createAccount.Service> c, String name) {
        for (createAccount.Service o : c) {
            if (o != null && o.getName().equalsIgnoreCase(name)) {
                return true;
            }
        }
        return false;
    }

    public boolean hasPredefinedService(Collection<createAccount.Service> c) {
        for (createAccount.Service o : c) {
            if (o != null && o.getType().equalsIgnoreCase("P")) {
                return true;
            }
        }
        return false;
    }


    void setAdapter_Suggested() {
        suggestedAdapter = new SuggestedRecyclerAdapter(getActivity(), suggestedServices.getServices());
        RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        suggestion_services_RV.setLayoutManager(mLayoutManager);
        suggestion_services_RV.setAdapter(suggestedAdapter);
        suggestion_services_RV.addOnItemTouchListener(
                new RecyclerItemClickListener(getActivity(), new RecyclerItemClickListener.OnItemClickListener() {
                    @Override
                    public void onItemClick(View view, int position) {
                        // TODO Handle item click
                        // service.add(suggestedServices.get(position));
                        service_name_ET.setText(suggestedServices.getServices().get(position).getName());
                        enter_price_ET.setText(suggestedServices.getServices().get(position).getPrice() + "");
                        //editPosition = position;
                        if (suggestedServices.getServices().get(position).getType().equals("P")) {
                            editPredefined = true;
                            service_name_ET.setEnabled(false);
                            enter_price_ET.requestFocus();
                            editPosition = position;
                            enter_price_ET.setSelection(enter_price_ET.getText().length());
                        } else {
                            editPredefined = false;
                        }

                    }
                })
        );
    }


    private void getSuggestedServices() {
        suggestedServicesApi = apiInterfaceAuth.suggestedServices(String.valueOf(store.getInt(Const.USER_ID)));
        apiHitAndHandle.makeApiCall(suggestedServicesApi, this);
    }

    private void AddServices(JSONObject service) {
        addService = apiInterfaceAuth.addService(returnRequestBodyJson(service.toString()));
        apiHitAndHandle.makeApiCall(addService, this);
    }

    @Override
    public void onSuccess(retrofit2.Call call, Object object) {
        super.onSuccess(call, object);
        if (call == suggestedServicesApi) {
            Gson gson = new Gson();
            suggestedServices = gson.fromJson(object.toString(), SuggestedServices.class);
            List<createAccount.Service> suggested = suggestedServices.getServices();
            setAdapter_Suggested();
        }
        if (call == addService) {
            Gson gson = new Gson();
            ServiceAdded servicesAdded = gson.fromJson(object.toString(), ServiceAdded.class);
            if (servicesAdded.getStatus() == 200) {
                getFragmentManager().popBackStack(null, FragmentManager.POP_BACK_STACK_INCLUSIVE);
                replaceFragmentNull(R.id.container, new AddBankDetailFragment(), "AddBankDetailFragment");

            } else {
                showMessage(getActivity(), "", servicesAdded.getMsg(), new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        dialog.dismiss();
                    }
                });
            }

        }
    }

    private JSONObject AddService() {
        if (hasPredefinedService(service)) {
            // ( { "user_id":"1", "services":[ {"id":"C","name":"abc", "price":100},{"id":"9","name":"x434yz", "price":100},{"id":"C","name":"abhinav", "price":100}] } )
            JSONObject serviceObject = new JSONObject();
            try {
                serviceObject.put("user_id", String.valueOf(store.getInt(Const.USER_ID)));
                JSONArray services = new JSONArray();
                for (int i = 0; i < service.size(); i++) {
                    JSONObject singleservice = new JSONObject();
                    singleservice.put("name", service.get(i).getName());
                    singleservice.put("price", service.get(i).getPrice());
                    if (service.get(i).getType().equals("P")) {
                        singleservice.put("id", service.get(i).getServiceId());
                    } else {
                        singleservice.put("id", service.get(i).getType());
                    }
                    services.put(singleservice);
                }
                serviceObject.put("services", services);

            } catch (JSONException e) {
                e.printStackTrace();
            }
            return serviceObject;
        } else {
    return null;
        }

    }


    @Override
    public void edit(int position) {
        edit = true;
        service_name_ET.setText(service.get(position).getName());
        enter_price_ET.setText(service.get(position).getPrice() + "");
        editPosition = position;
        if (service.get(position).getType().equals("P")) {
            editPredefined = true;
            service_name_ET.setEnabled(false);
            enter_price_ET.requestFocus();
            enter_price_ET.setSelection(enter_price_ET.getText().length());
        } else {
            editPredefined = false;
            service_name_ET.setEnabled(false);
            enter_price_ET.requestFocus();
            enter_price_ET.setSelection(enter_price_ET.getText().length());
        }

    }

    @Override
    public void delete(int position) {
        if (service.get(position).getType().equals("P")) {
            suggestedServices.getServices().add(service.get(position));
        }
        suggestedAdapter.notifyDataSetChanged();
        service.remove(position);
        servicesAdapter.notifyDataSetChanged();
        setHint();
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
        for (int i = 0; i <service.size(); i++) {

         //   Log.e("----Type----",service.get(i).getType());
            if (!service.get(i).getType().equals("P")) {
                hasCustom = false;
                break;
            } else {
                hasCustom = true;

            }
        }

        return hasCustom;
    }
}
