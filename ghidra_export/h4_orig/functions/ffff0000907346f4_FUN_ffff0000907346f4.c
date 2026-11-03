// ffff0000907346f4  size=308  FUN_ffff0000907346f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907346f4(void)

{
  long lVar1;
  char *pcVar2;
  uint uVar3;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  lVar1 = FUN_ffff00009071a348(s_security_booker_tag_ffff0000907cf7c3,0);
  if (lVar1 == 0) {
    FUN_ffff00009077e11c
              (s___s__suppress_mte_due_to_no_rese_ffff0000907f2c90,
               s_mte_info_bl2_ext_ffff0000907f9cbf);
  }
  if ((_DAT_ffff00009086dac0 & 6) == 0) {
    FUN_ffff00009077e11c(s___s__suppress_mte_ffff000090800bcd,s_mte_info_bl2_ext_ffff0000907f9cbf);
    _DAT_ffff00000c2cf5f4 = _DAT_ffff00000c2cf5f4 & 0xfffffff0 | 0xe;
  }
  else {
    uVar3 = 0xe;
    if (lVar1 != 0) {
      uVar3 = 0xf;
    }
    _DAT_ffff00000c2cf5f4 = _DAT_ffff00000c2cf5f4 & 0xfffffff0 | uVar3;
    if (lVar1 != 0) {
      if ((DAT_ffff00009086dac0 >> 2 & 1) != 0) {
        FUN_ffff000090719078(s_kasan_stacktrace_on_kasan_stack__ffff0000907e13af);
        uVar3 = _DAT_ffff00009086dac0 >> 8 & 0xf;
        if (uVar3 != 0) {
          local_30 = 0;
          uStack_28 = 0;
          local_40 = 0;
          uStack_38 = 0;
          FUN_ffff00009077f6fc
                    (&local_40,0x20,s_kasan_page_alloc_sample__u_ffff0000907efb28,1 << (ulong)uVar3)
          ;
          FUN_ffff000090719078(&local_40);
        }
        return;
      }
      pcVar2 = s_kasan_off_ffff0000907dc82c;
      goto LAB_ffff0000907347bc;
    }
  }
  pcVar2 = s_arm64_nomte_ffff0000907df9c3;
LAB_ffff0000907347bc:
  FUN_ffff000090719078(pcVar2);
  return;
}


