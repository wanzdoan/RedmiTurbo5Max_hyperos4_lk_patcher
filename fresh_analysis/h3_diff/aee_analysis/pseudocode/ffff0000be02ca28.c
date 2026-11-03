
void FUN_ffff0000be02ca28(undefined8 param_1)

{
  uint uVar1;
  long lVar2;
  ulong uVar3;
  long lVar4;
  undefined8 *puVar5;
  ulong uVar6;
  ulong uVar7;
  
  uVar1 = DAT_ffff0000be118380;
  uVar7 = (ulong)DAT_ffff0000be118380;
  lVar2 = FUN_ffff0000be05f1bc(uVar7 * 0x38);
  DAT_ffff0000be118400 = lVar2;
  if (lVar2 == 0) {
    FUN_ffff0000be05fd98
              (s__s__malloc_failed_for_lastpc_ffff0000be0ac8e5,
               s_dfd_internal_dump_decode_lastpc_ffff0000be0be395);
    return;
  }
  if (uVar1 != 0) {
    if (uVar1 == 1) {
      uVar3 = 0;
    }
    else {
      uVar3 = uVar7 & 0xfffffffe;
      puVar5 = (undefined8 *)(lVar2 + 0x38);
      uVar6 = uVar3;
      do {
        puVar5[-7] = 0;
        puVar5[-6] = 0;
        uVar6 = uVar6 - 2;
        *puVar5 = 0;
        puVar5[1] = 0;
        puVar5[-4] = 0;
        puVar5[-3] = 0;
        puVar5[3] = 0;
        puVar5[4] = 0;
        puVar5[-2] = 0;
        puVar5[-1] = 0;
        puVar5[5] = 0;
        puVar5[6] = 0;
        puVar5 = puVar5 + 0xe;
      } while (uVar6 != 0);
      if (uVar3 == uVar7) goto LAB_ffff0000be02caf0;
    }
    lVar4 = uVar7 - uVar3;
    puVar5 = (undefined8 *)(lVar2 + uVar3 * 0x38 + 0x18);
    do {
      puVar5[-3] = 0;
      puVar5[-2] = 0;
      lVar4 = lVar4 + -1;
      *puVar5 = 0;
      puVar5[1] = 0;
      puVar5[2] = 0;
      puVar5[3] = 0;
      puVar5 = puVar5 + 7;
    } while (lVar4 != 0);
  }
LAB_ffff0000be02caf0:
  if (DAT_ffff0000be1183d8 != (code *)0x0) {
                    /* WARNING: Could not recover jumptable at 0xffff0000be02cb0c. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
    (*DAT_ffff0000be1183d8)(param_1);
    return;
  }
  return;
}


