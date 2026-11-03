
ulong FUN_ffff0000b80176c4(uint param_1,undefined8 param_2)

{
  long lVar1;
  uint uVar2;
  long *plVar3;
  ulong uVar4;
  long *plVar5;
  long lVar6;
  code *UNRECOVERED_JUMPTABLE;
  ulong uVar7;
  
  if ((int)param_1 < 0) {
    uVar2 = FUN_ffff0000b80176c4(0);
    uVar4 = (ulong)uVar2;
    if (uVar2 == 0) {
      uVar2 = FUN_ffff0000b80176c4(1);
      uVar4 = (ulong)uVar2;
      if (uVar2 == 0) {
        uVar2 = FUN_ffff0000b80176c4(2);
        uVar4 = (ulong)uVar2;
        if (uVar2 == 0) {
          uVar2 = FUN_ffff0000b80176c4(3);
          uVar4 = (ulong)uVar2;
          if (uVar2 == 0) {
            return 0;
          }
        }
      }
    }
    FUN_ffff0000b804c33c
              (s__GZ_INIT__MBLOCK___s__failed_to_g_ffff0000b808eac4,
               s_gz_mblock_free_ffff0000b8091725,4);
    return uVar4;
  }
  uVar4 = (ulong)param_1;
  lVar1 = uVar4 * 0x48;
  plVar5 = (long *)(&DAT_ffff0000b80e8bd8 + lVar1);
  if (3 < param_1) {
    FUN_ffff0000b804c33c
              (s__GZ_INIT__MBLOCK___s__failed_to_g_ffff0000b80932b1,
               s_gz_mblock_create_ffff0000b808ea6e);
    return 0xffffffd7;
  }
  if (*plVar5 != 0) {
    FUN_ffff0000b804c33c
              (s__GZ_INIT__MBLOCK___s__mblock_alr_ffff0000b8099ac2,
               s_gz_mblock_create_ffff0000b808ea6e);
    return 0xfffffff2;
  }
  if ((code *)(&PTR_FUN_ffff0000b80e8c10)[uVar4 * 9] != (code *)0x0) {
    uVar2 = (*(code *)(&PTR_FUN_ffff0000b80e8c10)[uVar4 * 9])(plVar5,param_2,param_1);
    if (uVar2 != 0) {
      return (ulong)uVar2;
    }
  }
  if ((&DAT_ffff0000b80e8c08)[lVar1] == '\0') {
    if (*(long *)(&DAT_ffff0000b80e8be0 + lVar1) == 0) {
      uVar7 = 0;
      UNRECOVERED_JUMPTABLE = (code *)(&PTR_FUN_ffff0000b80e8c18)[uVar4 * 9];
      if (UNRECOVERED_JUMPTABLE == (code *)0x0) {
        return 0;
      }
      goto LAB_ffff0000b801785c;
    }
    lVar6 = FUN_ffff0000b800c9f4
                      (*(long *)(&DAT_ffff0000b80e8be0 + lVar1),
                       *(undefined8 *)(&DAT_ffff0000b80e8be8 + lVar1),
                       *(undefined8 *)(&DAT_ffff0000b80e8bf0 + lVar1),0,
                       *(undefined4 *)(&DAT_ffff0000b80e8bf8 + lVar1),
                       (&PTR_DAT_ffff0000b80e8c00)[uVar4 * 9]);
    plVar3 = (long *)0x0;
    if (lVar6 == 0) goto LAB_ffff0000b8017834;
LAB_ffff0000b80177e0:
    uVar7 = 0;
    *plVar5 = lVar6;
    if ((&DAT_ffff0000b80e8c08)[lVar1] == '\0') goto LAB_ffff0000b8017854;
    lVar6 = plVar3[2];
    *(long *)(&DAT_ffff0000b80e8be0 + lVar1) = plVar3[1];
    *(int *)(&DAT_ffff0000b80e8bf8 + lVar1) = (int)lVar6;
    UNRECOVERED_JUMPTABLE = (code *)(&PTR_FUN_ffff0000b80e8c18)[uVar4 * 9];
  }
  else {
    plVar3 = (long *)FUN_ffff0000b800db20((&PTR_DAT_ffff0000b80e8c00)[uVar4 * 9],0);
    if ((plVar3 != (long *)0x0) && (lVar6 = *plVar3, lVar6 != 0)) goto LAB_ffff0000b80177e0;
LAB_ffff0000b8017834:
    FUN_ffff0000b804c33c
              (s__GZ_INIT__MBLOCK___s___s_memory_0_ffff0000b808ea7f,
               s_gz_mblock_create_ffff0000b808ea6e,(&PTR_DAT_ffff0000b80e8c00)[uVar4 * 9],*plVar5,
               *(undefined8 *)(&DAT_ffff0000b80e8be0 + lVar1));
    uVar7 = 0xfffffffb;
LAB_ffff0000b8017854:
    UNRECOVERED_JUMPTABLE = (code *)(&PTR_FUN_ffff0000b80e8c18)[uVar4 * 9];
  }
  if (UNRECOVERED_JUMPTABLE == (code *)0x0) {
    return uVar7;
  }
LAB_ffff0000b801785c:
                    /* WARNING: Could not recover jumptable at 0xffff0000b8017870. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
  uVar4 = (*UNRECOVERED_JUMPTABLE)(plVar5,uVar7);
  return uVar4;
}


