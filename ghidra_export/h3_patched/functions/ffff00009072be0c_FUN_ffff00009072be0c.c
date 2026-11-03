// ffff00009072be0c  size=188  FUN_ffff00009072be0c

void FUN_ffff00009072be0c(void)

{
  int iVar1;
  undefined4 local_28;
  undefined4 local_24;
  undefined8 local_8;
  
  local_8 = 0;
  local_24 = 0;
  local_28 = 0;
  iVar1 = FUN_ffff00009072bec8(0x10,&local_8,&local_24,&local_28);
  if (iVar1 == 0) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_scp_smem_base_ap_ba_ffff0000907d5e76,
               s_ccci_scp_info_to_tfa_ffff0000907d9053,0x371,local_8,local_24,local_28);
    FUN_ffff0000907388b0(1,local_8,local_28);
    FUN_ffff00009077de54
              (s__s__d___CCCI_scp_set_dram_region_ffff000090800749,
               s_ccci_scp_info_to_tfa_ffff0000907d9053,0x375);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d___CCCI__s_scp_get_smem_inf_ffff0000907ddd48,
               s_ccci_scp_info_to_tfa_ffff0000907d9053,0x381,s_ccci_scp_info_to_tfa_ffff0000907d9053
               ,iVar1);
  }
  return;
}


