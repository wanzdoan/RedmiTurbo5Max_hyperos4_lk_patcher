// ffff000090732394  size=272  FUN_ffff000090732394

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff000090732394(long param_1,long param_2,int param_3)

{
  long lVar1;
  uint uVar2;
  uint uVar3;
  char *pcVar4;
  undefined8 uVar5;
  ulong uVar6;
  
  uVar3 = _DAT_ffff00009086da88;
  if (param_3 == 0) {
    pcVar4 = s__CCCI___s__d__tag_info_size_is_0_ffff0000907e2ce1;
    uVar5 = 0x6d;
  }
  else {
    if ((param_1 != 0) && (param_2 != 0)) {
      uVar2 = param_3 + _DAT_ffff00009086da88 + 0x53 & 0xfffffff8;
      if (0xffff < (int)uVar2) {
        FUN_ffff000090730228
                  (s__CCCI___s__d__not_enought_memory_ffff0000907f819a,
                   s_ccci_insert_tag_inf_ffff0000907e2d03,0x79,0x10000 - uVar2);
        return 0xffffffff;
      }
      uVar6 = 0;
      lVar1 = _DAT_ffff00009086da80 + (ulong)_DAT_ffff00009086da88;
      do {
        if (*(char *)(param_1 + uVar6) == '\0') break;
        *(char *)(lVar1 + uVar6) = *(char *)(param_1 + uVar6);
        uVar6 = uVar6 + 1;
      } while (uVar6 != 0x3f);
      *(undefined1 *)(lVar1 + (uVar6 & 0xffffffff)) = 0;
      *(uint *)(lVar1 + 0x48) = uVar2;
      *(uint *)(lVar1 + 0x40) = uVar3 + 0x4c;
      *(int *)(lVar1 + 0x44) = param_3;
      FUN_ffff00009077eb20(lVar1 + 0x4c,param_2,param_3);
      _DAT_ffff00009086da88 = uVar2;
      _DAT_ffff00009086da94 = _DAT_ffff00009086da94 + 1;
      return 0;
    }
    pcVar4 = s__CCCI___s__d__name_or_data_is_NU_ffff0000907dab92;
    uVar5 = 0x72;
  }
  FUN_ffff000090730228(pcVar4,s_ccci_insert_tag_inf_ffff0000907e2d03,uVar5);
  return 0;
}


