// ffff000090731dd8  size=268  FUN_ffff000090731dd8

undefined8 FUN_ffff000090731dd8(ulong param_1)

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
  FUN_ffff00009077de54
            (s__s__d___CCCI___s__addr__0x_llX_ffff0000907e11f0,
             s_ccci_apply_md_sib_remapping_ffff0000907d790b,0x52,
             s_ccci_apply_md_sib_remapping_ffff0000907d790b,param_1);
  FUN_ffff00009071ea6c(0xc200040b,4,param_1 & 0xffffffff,param_1 >> 0x20,0,0,0,0,&local_40);
  if (local_40 == -0xf) {
    FUN_ffff00009077de54
              (s__s__d___CCCI___s__error__MD_SIB__ffff0000907f2a80,
               s_ccci_apply_md_sib_remapping_ffff0000907d790b,0x58,
               s_ccci_apply_md_sib_remapping_ffff0000907d790b,0xfffffffffffffff1);
    uVar1 = 0xffffffff;
  }
  else {
    FUN_ffff00009077de54
              (s__s__d___CCCI_SIBLOG_BANK_MAP0_va_ffff0000907cf6ce,
               s_ccci_apply_md_sib_remapping_ffff0000907d790b,0x5c,local_38);
    FUN_ffff00009077de54
              (s__s__d___CCCI_SIBLOG_BANK_MAP1_va_ffff0000907d2b61,
               s_ccci_apply_md_sib_remapping_ffff0000907d790b,0x5d,local_30);
    FUN_ffff00009077de54
              (s__s__d___CCCI_SIBLOG_BANK_MAP2_va_ffff0000907d7927,
               s_ccci_apply_md_sib_remapping_ffff0000907d790b,0x5e,local_28);
    uVar1 = 0;
  }
  return uVar1;
}


