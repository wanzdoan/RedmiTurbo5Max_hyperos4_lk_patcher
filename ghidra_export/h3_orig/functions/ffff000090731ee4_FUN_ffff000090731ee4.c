// ffff000090731ee4  size=180  FUN_ffff000090731ee4

void FUN_ffff000090731ee4(void)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009077de54
            (s__s__d___CCCI_start_ffff0000907e78c0,s_ccci_set_hw_locked_ffff0000907f2ab6,0x68);
  FUN_ffff00009071ea6c(0xc200040b,8,0,0,0,0,0,0,local_30);
  if (local_30[0] == -0xf) {
    FUN_ffff00009077de54
              (s__s__d___CCCI_error__MD_HW_REMAP__ffff0000907fb5d4,
               s_ccci_set_hw_locked_ffff0000907f2ab6,0x6e,0xfffffffffffffff1);
  }
  else {
    FUN_ffff00009077de54
              (s__s__d___CCCI_call_MD_HW_REMAP_LO_ffff0000907f46d6,
               s_ccci_set_hw_locked_ffff0000907f2ab6,0x70);
  }
  return;
}


