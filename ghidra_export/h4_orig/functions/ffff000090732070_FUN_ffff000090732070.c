// ffff000090732070  size=268  FUN_ffff000090732070

undefined8 FUN_ffff000090732070(ulong param_1)

{
  undefined8 uVar1;
  long local_40;
  undefined8 local_38;
  undefined8 local_30;
  undefined8 local_28;
  
  local_30 = 0;
  local_28 = 0;
  local_40 = 0;
  local_38 = 0;
  FUN_ffff00009077e11c
            (s__s__d___CCCI___s__addr__0x_llX_ffff0000907e12c1,
             s_ccci_apply_md_sib_remapping_ffff0000907d79a1,0x52,
             s_ccci_apply_md_sib_remapping_ffff0000907d79a1,param_1);
  FUN_ffff00009071ebf4(0xc200040b,4,param_1 & 0xffffffff,param_1 >> 0x20,0,0,0,0,&local_40);
  if (local_40 == -0xf) {
    FUN_ffff00009077e11c
              (s__s__d___CCCI___s__error__MD_SIB__ffff0000907f2c0c,
               s_ccci_apply_md_sib_remapping_ffff0000907d79a1,0x58,
               s_ccci_apply_md_sib_remapping_ffff0000907d79a1,0xfffffffffffffff1);
    uVar1 = 0xffffffff;
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d___CCCI_SIBLOG_BANK_MAP0_va_ffff0000907cf6ed,
               s_ccci_apply_md_sib_remapping_ffff0000907d79a1,0x5c,local_38);
    FUN_ffff00009077e11c
              (s__s__d___CCCI_SIBLOG_BANK_MAP1_va_ffff0000907d2b9e,
               s_ccci_apply_md_sib_remapping_ffff0000907d79a1,0x5d,local_30);
    FUN_ffff00009077e11c
              (s__s__d___CCCI_SIBLOG_BANK_MAP2_va_ffff0000907d79bd,
               s_ccci_apply_md_sib_remapping_ffff0000907d79a1,0x5e,local_28);
    uVar1 = 0;
  }
  return uVar1;
}


