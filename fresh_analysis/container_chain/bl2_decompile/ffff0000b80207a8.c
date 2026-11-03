
undefined8 FUN_ffff0000b80207a8(ulong param_1,undefined8 param_2,undefined4 param_3)

{
  code *UNRECOVERED_JUMPTABLE;
  undefined8 uVar1;
  int *piVar2;
  ulong uVar3;
  long lVar4;
  long *plVar5;
  
  uVar3 = 1L << (param_1 & 0x3f);
  if ((((((((DAT_ffff0000b811b690 != 0) &&
           (plVar5 = &DAT_ffff0000b811b690, (uVar3 & DAT_ffff0000b811b698) != 0)) ||
          ((DAT_ffff0000b811b6a0 != 0 &&
           (plVar5 = &DAT_ffff0000b811b6a0, (uVar3 & DAT_ffff0000b811b6a8) != 0)))) ||
         ((DAT_ffff0000b811b6b0 != 0 &&
          (plVar5 = &DAT_ffff0000b811b6b0, (uVar3 & DAT_ffff0000b811b6b8) != 0)))) ||
        (((DAT_ffff0000b811b6c0 != 0 &&
          (plVar5 = &DAT_ffff0000b811b6c0, (uVar3 & DAT_ffff0000b811b6c8) != 0)) ||
         ((((DAT_ffff0000b811b6d0 != 0 &&
            (plVar5 = &DAT_ffff0000b811b6d0, (uVar3 & DAT_ffff0000b811b6d8) != 0)) ||
           ((DAT_ffff0000b811b6e0 != 0 &&
            (plVar5 = &DAT_ffff0000b811b6e0, (uVar3 & DAT_ffff0000b811b6e8) != 0)))) ||
          ((DAT_ffff0000b811b6f0 != 0 &&
           (plVar5 = &DAT_ffff0000b811b6f0, (uVar3 & DAT_ffff0000b811b6f8) != 0)))))))) ||
       ((DAT_ffff0000b811b700 != 0 &&
        (plVar5 = &DAT_ffff0000b811b700, (uVar3 & DAT_ffff0000b811b708) != 0)))) ||
      (((DAT_ffff0000b811b710 != 0 &&
        (plVar5 = &DAT_ffff0000b811b710, (uVar3 & DAT_ffff0000b811b718) != 0)) ||
       ((DAT_ffff0000b811b720 != 0 &&
        (plVar5 = &DAT_ffff0000b811b720, (uVar3 & DAT_ffff0000b811b728) != 0)))))) &&
     ((lVar4 = *plVar5, lVar4 != 0 &&
      (uVar3 = (ulong)*(uint *)(lVar4 + 0x10), 0 < (int)*(uint *)(lVar4 + 0x10))))) {
    piVar2 = *(int **)(lVar4 + 8);
    do {
      if (*piVar2 == (int)param_1) {
        UNRECOVERED_JUMPTABLE = (code *)&LAB_ffff0000b802061c;
        if (*(code **)(lVar4 + 0x30) != (code *)0x0) {
          UNRECOVERED_JUMPTABLE = *(code **)(lVar4 + 0x30);
        }
                    /* WARNING: Could not recover jumptable at 0xffff0000b8020954. Too many branches
                        */
                    /* WARNING: Treating indirect jump as call */
        uVar1 = (*UNRECOVERED_JUMPTABLE)(lVar4,piVar2,param_2,param_3);
        return uVar1;
      }
      uVar3 = uVar3 - 1;
      piVar2 = piVar2 + 0xf;
    } while (uVar3 != 0);
  }
  return 0xfffffff8;
}


