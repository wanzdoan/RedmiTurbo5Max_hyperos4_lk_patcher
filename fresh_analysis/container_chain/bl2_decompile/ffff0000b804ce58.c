
void FUN_ffff0000b804ce58(undefined8 *param_1,undefined8 *param_2,ulong param_3)

{
  undefined8 *puVar1;
  uint uVar2;
  uint uVar3;
  undefined8 *puVar4;
  int iVar5;
  
  uVar3 = (uint)param_1;
  if ((param_1 != param_2) && (param_3 != 0)) {
    if ((long)param_1 < (long)param_2) {
      if ((((uint)param_2 | uVar3) & 7) != 0) {
        uVar2 = (uint)param_3;
        if ((((uint)param_2 ^ uVar3) & 7) == 0 && 7 < param_3) {
          uVar2 = 8 - (uVar3 & 7);
        }
        param_3 = param_3 - (long)(int)uVar2;
        if (0 < (int)uVar2) {
          uVar2 = uVar2 + 1;
          puVar1 = param_2;
          puVar4 = param_1;
          do {
            param_2 = (undefined8 *)((long)puVar1 + 1);
            uVar2 = uVar2 - 1;
            param_1 = (undefined8 *)((long)puVar4 + 1);
            *(undefined1 *)puVar4 = *(undefined1 *)puVar1;
            puVar1 = param_2;
            puVar4 = param_1;
          } while (1 < uVar2);
        }
      }
      iVar5 = (int)(param_3 >> 3);
      if (0 < iVar5) {
        uVar3 = iVar5 + 1;
        puVar1 = param_2;
        puVar4 = param_1;
        do {
          param_2 = puVar1 + 1;
          uVar3 = uVar3 - 1;
          param_1 = puVar4 + 1;
          *puVar4 = *puVar1;
          puVar1 = param_2;
          puVar4 = param_1;
        } while (1 < uVar3);
      }
      uVar2 = (uint)param_3;
      uVar3 = uVar2 & 7;
      if (((((param_3 & 7) != 0) && (*(undefined1 *)param_1 = *(undefined1 *)param_2, 1 < uVar3)) &&
          ((*(undefined1 *)((long)param_1 + 1) = *(undefined1 *)((long)param_2 + 1),
           uVar3 - 3 < 0xfffffffe &&
           (((*(undefined1 *)((long)param_1 + 2) = *(undefined1 *)((long)param_2 + 2),
             (uVar2 & 6) != 2 &&
             (*(undefined1 *)((long)param_1 + 3) = *(undefined1 *)((long)param_2 + 3),
             uVar3 - 5 < 0xfffffffe)) &&
            (*(undefined1 *)((long)param_1 + 4) = *(undefined1 *)((long)param_2 + 4),
            (uVar2 & 6) != 4)))))) &&
         (*(undefined1 *)((long)param_1 + 5) = *(undefined1 *)((long)param_2 + 5),
         uVar3 - 7 < 0xfffffffe)) {
        *(undefined1 *)((long)param_1 + 6) = *(undefined1 *)((long)param_2 + 6);
      }
    }
    else {
      param_1 = (undefined8 *)((long)param_1 + param_3);
      param_2 = (undefined8 *)((long)param_2 + param_3);
      uVar3 = (uint)param_1;
      if (((uVar3 | (uint)param_2) & 7) != 0) {
        uVar2 = (uint)param_3;
        if (((uVar3 ^ (uint)param_2) & 7) == 0 && 8 < param_3) {
          uVar2 = uVar3 & 7;
        }
        param_3 = param_3 - (long)(int)uVar2;
        if (0 < (int)uVar2) {
          uVar2 = uVar2 + 1;
          do {
            param_2 = (undefined8 *)((long)param_2 + -1);
            uVar2 = uVar2 - 1;
            param_1 = (undefined8 *)((long)param_1 + -1);
            *(undefined1 *)param_1 = *(undefined1 *)param_2;
          } while (1 < uVar2);
        }
      }
      iVar5 = (int)(param_3 >> 3);
      if (0 < iVar5) {
        uVar3 = iVar5 + 1;
        do {
          param_2 = param_2 + -1;
          uVar3 = uVar3 - 1;
          param_1 = param_1 + -1;
          *param_1 = *param_2;
        } while (1 < uVar3);
      }
      uVar2 = (uint)param_3;
      uVar3 = uVar2 & 7;
      if (((((param_3 & 7) != 0) &&
           (*(undefined1 *)((long)param_1 + -1) = *(undefined1 *)((long)param_2 + -1), 1 < uVar3))
          && (*(undefined1 *)((long)param_1 + -2) = *(undefined1 *)((long)param_2 + -2),
             uVar3 - 3 < 0xfffffffe)) &&
         (((*(undefined1 *)((long)param_1 + -3) = *(undefined1 *)((long)param_2 + -3),
           (uVar2 & 6) != 2 &&
           (*(undefined1 *)((long)param_1 + -4) = *(undefined1 *)((long)param_2 + -4),
           uVar3 - 5 < 0xfffffffe)) &&
          ((*(undefined1 *)((long)param_1 + -5) = *(undefined1 *)((long)param_2 + -5),
           (uVar2 & 6) != 4 &&
           (*(undefined1 *)((long)param_1 + -6) = *(undefined1 *)((long)param_2 + -6),
           uVar3 - 7 < 0xfffffffe)))))) {
        *(undefined1 *)((long)param_1 + -7) = *(undefined1 *)((long)param_2 + -7);
      }
    }
  }
  return;
}


