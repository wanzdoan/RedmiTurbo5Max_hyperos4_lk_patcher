// ffff000090731828  size=380  FUN_ffff000090731828

ulong FUN_ffff000090731828(undefined4 param_1,long param_2,int param_3,int param_4,int param_5)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  char *pcVar4;
  undefined8 uVar5;
  long lVar6;
  undefined8 *puVar7;
  uint *puVar8;
  uint uVar9;
  
  uVar1 = FUN_ffff000090731400();
  if (uVar1 == 0) {
    pcVar4 = s__CCCI___s__d__Get_zero_flag_ffff0000907e606f;
    uVar5 = 0x95;
  }
  else {
    if (param_4 < param_3) {
      uVar9 = param_4 + 1;
      puVar7 = (undefined8 *)(param_2 + (long)param_4 * 0x18 + 0x10);
      do {
        if (((*(uint *)(puVar7 + -1) & uVar1) != 0) &&
           ((*(byte *)((long)puVar7 + -4) >> 4 & 1) == 0)) {
          uVar5 = *puVar7;
          iVar2 = *(int *)((long)puVar7 + -0xc);
          if ((int)uVar9 < param_3) {
            lVar6 = (long)(int)uVar9;
            puVar8 = (uint *)(param_2 + (long)(int)uVar9 * 0x18 + 8);
            do {
              uVar9 = uVar9 + 1;
              if ((*puVar8 & uVar1) == 0) {
                iVar2 = FUN_ffff0000907319a4(param_1,uVar5,iVar2,param_5 == 0);
                return (ulong)(uint)(iVar2 >> 0x1f);
              }
              if (((byte)puVar8[1] >> 4 & 1) != 0) {
                iVar2 = FUN_ffff0000907319a4(param_1,uVar5,iVar2,param_5 == 0);
                if (iVar2 < 0) {
                  uVar9 = 0xffffffff;
                }
                return (ulong)uVar9;
              }
              lVar6 = lVar6 + 1;
              iVar2 = puVar8[-1] + iVar2;
              puVar8 = puVar8 + 6;
            } while (lVar6 < param_3);
          }
          uVar3 = FUN_ffff0000907319a4(param_1,uVar5,iVar2,param_5 == 0);
          return uVar3;
        }
        uVar9 = uVar9 + 1;
        puVar7 = puVar7 + 3;
      } while (uVar9 - param_3 != 1);
    }
    pcVar4 = s__CCCI___s__d__Can_t_find_proper_b_ffff0000907e47f1;
    uVar5 = 0x9c;
  }
  FUN_ffff00009072ff90(pcVar4,s_md_region_parse_helper_ffff0000907f6239,uVar5);
  return 0xffffffff;
}


