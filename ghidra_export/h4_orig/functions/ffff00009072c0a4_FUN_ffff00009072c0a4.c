// ffff00009072c0a4  size=188  FUN_ffff00009072c0a4

void FUN_ffff00009072c0a4(void)

{
  int iVar1;
  undefined4 local_28;
  undefined4 local_24;
  undefined8 local_8;
  
  local_8 = 0;
  local_24 = 0;
  local_28 = 0;
  iVar1 = FUN_ffff00009072c160(0x10,&local_8,&local_24,&local_28);
  if (iVar1 == 0) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_scp_smem_base_ap_ba_ffff0000907d5f0c,
               s_ccci_scp_info_to_tfa_ffff0000907d90ea,0x371,local_8,local_24,local_28);
    FUN_ffff000090738b48(1,local_8,local_28);
    FUN_ffff00009077e11c
              (s__s__d___CCCI_scp_set_dram_region_ffff000090800925,
               s_ccci_scp_info_to_tfa_ffff0000907d90ea,0x375);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___CCCI__s_scp_get_smem_inf_ffff0000907dde02,
               s_ccci_scp_info_to_tfa_ffff0000907d90ea,0x381,s_ccci_scp_info_to_tfa_ffff0000907d90ea
               ,iVar1);
  }
  return;
}


