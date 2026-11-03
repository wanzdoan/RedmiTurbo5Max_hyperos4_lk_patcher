
undefined8 FUN_ffff0000b807aa14(long param_1)

{
  undefined8 uVar1;
  int iVar2;
  undefined4 uVar3;
  long lVar4;
  long *plVar5;
  
  if ((((param_1 != 0) && (*(long *)(param_1 + 0x40) != 0)) && (*(long *)(param_1 + 0x48) != 0)) &&
     ((plVar5 = *(long **)(param_1 + 0x38), plVar5 != (long *)0x0 && (*plVar5 == param_1)))) {
    iVar2 = (int)plVar5[1];
    if (((iVar2 - 0x39U < 0x39) &&
        ((1L << ((ulong)(iVar2 - 0x39U) & 0x3f) & 0x100400400011001U) != 0)) ||
       ((iVar2 == 0x29a || (iVar2 == 0x2a)))) {
      lVar4 = plVar5[2];
      iVar2 = (int)plVar5[6];
      *(undefined8 *)(param_1 + 0x10) = 0;
      *(undefined8 *)(param_1 + 0x28) = 0;
      *(undefined8 *)(param_1 + 0x30) = 0;
      *(undefined4 *)(param_1 + 0x58) = 2;
      plVar5[4] = lVar4;
      plVar5[5] = 0;
      if (iVar2 < 0) {
        iVar2 = -iVar2;
        *(int *)(plVar5 + 6) = iVar2;
      }
      uVar3 = 0x39;
      if (iVar2 != 2) {
        uVar3 = 0x2a;
      }
      *(undefined4 *)(plVar5 + 1) = uVar3;
      if (iVar2 != 2) {
        uVar1 = FUN_ffff0000b804683c(0,0,0);
      }
      else {
        uVar1 = FUN_ffff0000b8046ff8();
      }
      *(undefined8 *)(param_1 + 0x60) = uVar1;
      *(undefined4 *)((long)plVar5 + 0x4c) = 0xfffffffe;
      FUN_ffff0000b8080498(plVar5);
      return 0;
    }
  }
  return 0xfffffffe;
}


