// ffff00009073445c  size=308  FUN_ffff00009073445c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff00009073445c(void)

{
  long lVar1;
  char *pcVar2;
  uint uVar3;
  undefined8 local_40;
  undefined8 uStack_38;
  undefined8 local_30;
  undefined8 uStack_28;
  
  lVar1 = FUN_ffff00009071a204(s_security_booker_tag_ffff0000907cf7a4,0);
  if (lVar1 == 0) {
    FUN_ffff00009077de54
              (s___s__suppress_mte_due_to_no_rese_ffff0000907f2aed,
               s_mte_info_bl2_ext_ffff0000907f9b23);
  }
  if ((_DAT_ffff00009086da28 & 6) == 0) {
    FUN_ffff00009077de54(s___s__suppress_mte_ffff0000908009f1,s_mte_info_bl2_ext_ffff0000907f9b23);
    _DAT_ffff00000c2cf5f4 = _DAT_ffff00000c2cf5f4 & 0xfffffff0 | 0xe;
  }
  else {
    uVar3 = 0xe;
    if (lVar1 != 0) {
      uVar3 = 0xf;
    }
    _DAT_ffff00000c2cf5f4 = _DAT_ffff00000c2cf5f4 & 0xfffffff0 | uVar3;
    if (lVar1 != 0) {
      if ((DAT_ffff00009086da28 >> 2 & 1) != 0) {
        FUN_ffff000090718f34(s_kasan_stacktrace_on_kasan_stack__ffff0000907e12c7);
        uVar3 = _DAT_ffff00009086da28 >> 8 & 0xf;
        if (uVar3 != 0) {
          local_30 = 0;
          uStack_28 = 0;
          local_40 = 0;
          uStack_38 = 0;
          FUN_ffff00009077f434
                    (&local_40,0x20,s_kasan_page_alloc_sample__u_ffff0000907ef9a0,1 << (ulong)uVar3)
          ;
          FUN_ffff000090718f34(&local_40);
        }
        return;
      }
      pcVar2 = s_kasan_off_ffff0000907dc73f;
      goto LAB_ffff000090734524;
    }
  }
  pcVar2 = s_arm64_nomte_ffff0000907df909;
LAB_ffff000090734524:
  FUN_ffff000090718f34(pcVar2);
  return;
}


