// ffff00009077605c  size=376  FUN_ffff00009077605c

undefined8 FUN_ffff00009077605c(long param_1,uint param_2)

{
  long *plVar1;
  long lVar2;
  long *plVar3;
  uint uVar4;
  int iVar5;
  
  if ((((param_1 != 0) && (*(long *)(param_1 + 0x40) != 0)) &&
      (*(code **)(param_1 + 0x48) != (code *)0x0)) &&
     (((plVar3 = *(long **)(param_1 + 0x38), plVar3 != (long *)0x0 && (*plVar3 == param_1)) &&
      ((int)plVar3[1] - 0x3f34U < 0x20)))) {
    if ((int)param_2 < 0) {
      if (param_2 < 0xfffffff1) {
        return 0xfffffffe;
      }
      iVar5 = 0;
      uVar4 = -param_2;
    }
    else {
      iVar5 = (param_2 >> 4) + 5;
      uVar4 = param_2 & 0xf;
      if (0x2f < param_2) {
        uVar4 = param_2;
      }
    }
    if ((uVar4 - 8 < 8) || (uVar4 == 0)) {
      if ((plVar3[9] == 0) || (*(uint *)(plVar3 + 7) == uVar4)) {
        *(int *)(plVar3 + 2) = iVar5;
        *(uint *)(plVar3 + 7) = uVar4;
      }
      else {
        (**(code **)(param_1 + 0x48))(*(undefined8 *)(param_1 + 0x50));
        lVar2 = *(long *)(param_1 + 0x40);
        plVar3[9] = 0;
        *(int *)(plVar3 + 2) = iVar5;
        *(uint *)(plVar3 + 7) = uVar4;
        if (lVar2 == 0) {
          return 0xfffffffe;
        }
      }
      if ((((*(long *)(param_1 + 0x48) != 0) &&
           (plVar3 = *(long **)(param_1 + 0x38), plVar3 != (long *)0x0)) && (*plVar3 == param_1)) &&
         ((int)plVar3[1] - 0x3f34U < 0x20)) {
        uVar4 = *(uint *)(plVar3 + 2);
        *(undefined8 *)((long)plVar3 + 0x3c) = 0;
        *(undefined4 *)((long)plVar3 + 0x44) = 0;
        plVar3[5] = 0;
        *(undefined8 *)(param_1 + 0x10) = 0;
        *(undefined8 *)(param_1 + 0x28) = 0;
        *(undefined8 *)(param_1 + 0x30) = 0;
        if (uVar4 != 0) {
          *(ulong *)(param_1 + 0x60) = (ulong)uVar4 & 1;
        }
        plVar3[6] = 0;
        plVar3[1] = 0x3f34;
        plVar1 = plVar3 + 0xab;
        *(undefined8 *)((long)plVar3 + 0x14) = 0xffffffff00000000;
        *(undefined4 *)((long)plVar3 + 0x1c) = 0x8000;
        plVar3[0x12] = (long)plVar1;
        plVar3[0xd] = (long)plVar1;
        plVar3[0xe] = (long)plVar1;
        plVar3[0x37d] = -0xffffffff;
        plVar3[10] = 0;
        *(undefined4 *)(plVar3 + 0xb) = 0;
        return 0;
      }
    }
  }
  return 0xfffffffe;
}


