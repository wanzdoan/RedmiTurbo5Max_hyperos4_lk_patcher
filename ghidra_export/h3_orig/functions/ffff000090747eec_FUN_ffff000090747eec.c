// ffff000090747eec  size=144  FUN_ffff000090747eec

void FUN_ffff000090747eec(undefined8 param_1,undefined4 param_2)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ea6c(0xc200041a,2,param_1,param_2,0,0,0,0,local_30);
  if (local_30[0] == 0x100) {
    FUN_ffff00009077de54
              (s___s__Set_gps_emi_addr_ok_ffff0000907fd6d5,
               s_conninfra_smc_set_gps_emi_addr_ffff0000907e6467);
  }
  else {
    FUN_ffff00009077de54
              (s___s__Fail_to_set_gps_emi_addr__r_ffff0000907d7c7c,
               s_conninfra_smc_set_gps_emi_addr_ffff0000907e6467);
  }
  return;
}


