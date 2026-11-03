// ffff00009073217c  size=180  FUN_ffff00009073217c

void FUN_ffff00009073217c(void)

{
  long local_30 [4];
  
  local_30[2] = 0;
  local_30[3] = 0;
  local_30[0] = 0;
  local_30[1] = 0;
  FUN_ffff00009077e11c
            (s__s__d___CCCI_start_ffff0000907e7a04,s_ccci_set_hw_locked_ffff0000907f2c42,0x68);
  FUN_ffff00009071ebf4(0xc200040b,8,0,0,0,0,0,0,local_30);
  if (local_30[0] == -0xf) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_error__MD_HW_REMAP__ffff0000907fb770,
               s_ccci_set_hw_locked_ffff0000907f2c42,0x6e,0xfffffffffffffff1);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_call_MD_HW_REMAP_LO_ffff0000907f4843,
               s_ccci_set_hw_locked_ffff0000907f2c42,0x70);
  }
  return;
}


