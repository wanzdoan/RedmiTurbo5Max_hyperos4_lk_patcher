// ffff000090748184  size=144  FUN_ffff000090748184

void FUN_ffff000090748184(undefined8 param_1,undefined4 param_2)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009071ebf4(0xc200041a,2,param_1,param_2,0,0,0,0,local_30);
  if (local_30[0] == 0x100) {
    FUN_ffff00009077e11c
              (s___s__Set_gps_emi_addr_ok_ffff0000907fd87d,
               s_conninfra_smc_set_gps_emi_addr_ffff0000907e657b);
  }
  else {
    FUN_ffff00009077e11c
              (s___s__Fail_to_set_gps_emi_addr__r_ffff0000907d7d12,
               s_conninfra_smc_set_gps_emi_addr_ffff0000907e657b);
  }
  return;
}


