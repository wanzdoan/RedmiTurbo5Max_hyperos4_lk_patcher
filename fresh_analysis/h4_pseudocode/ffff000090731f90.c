
undefined8 FUN_ffff000090731f90(ulong param_1,undefined4 param_2)

{
  undefined8 uVar1;
  long local_40;
  undefined8 local_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  local_30 = 0;
  uStack_28 = 0;
  local_40 = 0;
  local_38 = 0;
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s__addr__0x_llX_ffff0000907e12c1,
             s_ccci_apply_md_bank4_remap_by_slo_ffff0000907de08c,0x3d,
             s_ccci_apply_md_bank4_remap_by_slo_ffff0000907de08c,param_1);
  FUN_ffff00009071ebf4(0xc200040b,3,param_1 & 0xffffffff,param_1 >> 0x20,param_2,0,0,0,&local_40);
  if (local_40 == -0xf) {
    FUN_ffff000090730228
              (s__CCCI___s__d____s__error__MD_BAN_ffff0000907d7968,
               s_ccci_apply_md_bank4_remap_by_slo_ffff0000907de08c,0x45,
               s_ccci_apply_md_bank4_remap_by_slo_ffff0000907de08c,0xfffffffffffffff1);
    uVar1 = 0xffffffff;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_BANK4_MAP__d__value_ffff0000907fb748,
               s_ccci_apply_md_bank4_remap_by_slo_ffff0000907de08c,0x48,param_2,local_38);
    uVar1 = 0;
  }
  return uVar1;
}


