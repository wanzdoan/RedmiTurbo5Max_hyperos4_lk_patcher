// ffff000090731de4  size=428  FUN_ffff000090731de4

undefined8 FUN_ffff000090731de4(ulong param_1)

{
  char *pcVar1;
  undefined8 uVar2;
  long local_50;
  undefined8 local_48;
  undefined8 local_40;
  undefined8 local_38;
  
  local_40 = 0;
  local_38 = 0;
  local_50 = 0;
  local_48 = 0;
  FUN_ffff00009071ebf4(0xc200040b,1,param_1 & 0xffffffff,param_1 >> 0x20,0,0,0,0,&local_50);
  if (local_50 == -0xf) {
    pcVar1 = s__CCCI___s__d____s__error__MD_BAN_ffff0000907e92eb;
    uVar2 = 0x1c;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_BANK0_MAP0_value_0x_ffff0000907dc718,
               s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0x20,local_48);
    FUN_ffff00009077e11c
              (s__s__d___CCCI_BANK0_MAP1_value_0x_ffff0000907fb723,
               s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0x21,local_40);
    FUN_ffff00009077e11c
              (s__s__d___CCCI_BANK0_MAP2_value_0x_ffff0000907e129c,
               s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0x22,local_38);
    FUN_ffff00009071ebf4(0xc200040b,2,param_1 & 0xffffffff,param_1 >> 0x20,0,0,0,0,&local_50);
    if (local_50 != -0xf) {
      FUN_ffff00009077e11c
                (s__s__d___CCCI_BANK1_MAP0_value_0x_ffff0000907f9bad,
                 s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0x30);
      FUN_ffff00009077e11c
                (s__s__d___CCCI_BANK1_MAP1_value_0x_ffff0000907dab6d,
                 s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0x31,local_48);
      FUN_ffff00009077e11c
                (s__s__d___CCCI_BANK1_MAP2_value_0x_ffff0000907ff09c,
                 s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0x32,local_40);
      FUN_ffff00009077e11c
                (s__s__d___CCCI_BANK1_MAP3_value_0x_ffff0000907e9324,
                 s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0x33,local_38);
      return 0;
    }
    pcVar1 = s__CCCI___s__d____s__error__MD_BAN_ffff0000907f63da;
    uVar2 = 0x2c;
  }
  FUN_ffff000090730228
            (pcVar1,s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,uVar2,
             s_ccci_apply_md_bank0_1_hw_remap_ffff0000907e61b6,0xfffffffffffffff1);
  return 0xffffffff;
}


