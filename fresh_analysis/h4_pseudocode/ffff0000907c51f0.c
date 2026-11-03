
undefined8
FUN_ffff0000907c51f0(undefined8 param_1,long param_2,ulong param_3,long param_4,ulong *param_5)

{
  uint uVar1;
  int iVar2;
  ulong uVar3;
  int *piVar4;
  undefined8 uVar5;
  long lVar6;
  int *piVar7;
  
  if (param_2 == 0) {
    return 2;
  }
  if (param_4 == 0) {
    return 2;
  }
  if (param_5 == (ulong *)0x0) {
    return 2;
  }
  uVar3 = FUN_ffff0000907c62d8(2);
  if (((uVar3 & 1) == 0) || (piVar4 = (int *)FUN_ffff0000907c62ac(), piVar4 == (int *)0x0)) {
LAB_ffff0000907c5484:
    uVar5 = 2;
  }
  else {
    if (*piVar4 == -0x6f86f7fc) {
      uVar1 = piVar4[1];
      uVar3 = FUN_ffff00009077f250(param_2);
      if ((uVar3 == uVar1) &&
         (iVar2 = thunk_FUN_ffff00009077eae8(*(undefined8 *)(piVar4 + 4),param_2,(ulong)uVar1),
         iVar2 == 0)) {
        lVar6 = 0;
        piVar7 = piVar4;
LAB_ffff0000907c544c:
        if (param_3 < (uint)piVar7[2]) {
          return 7;
        }
        if (*(long *)(piVar4 + lVar6 * 8 + 6) != 0) {
          thunk_FUN_ffff00009077eb20(param_4);
          *param_5 = (ulong)(uint)piVar7[2];
          return 0;
        }
        goto LAB_ffff0000907c5484;
      }
      if (piVar4[8] == -0x6f86f7fc) {
        uVar1 = piVar4[9];
        uVar3 = FUN_ffff00009077f250(param_2);
        if ((uVar3 == uVar1) &&
           (iVar2 = thunk_FUN_ffff00009077eae8(*(undefined8 *)(piVar4 + 0xc),param_2,(ulong)uVar1),
           iVar2 == 0)) {
          lVar6 = 1;
          piVar7 = piVar4 + 8;
          goto LAB_ffff0000907c544c;
        }
        if (piVar4[0x10] == -0x6f86f7fc) {
          uVar1 = piVar4[0x11];
          uVar3 = FUN_ffff00009077f250(param_2);
          if ((uVar3 == uVar1) &&
             (iVar2 = thunk_FUN_ffff00009077eae8
                                (*(undefined8 *)(piVar4 + 0x14),param_2,(ulong)uVar1), iVar2 == 0))
          {
            lVar6 = 2;
            piVar7 = piVar4 + 0x10;
            goto LAB_ffff0000907c544c;
          }
          if (piVar4[0x18] == -0x6f86f7fc) {
            uVar1 = piVar4[0x19];
            uVar3 = FUN_ffff00009077f250(param_2);
            if ((uVar3 == uVar1) &&
               (iVar2 = thunk_FUN_ffff00009077eae8
                                  (*(undefined8 *)(piVar4 + 0x1c),param_2,(ulong)uVar1), iVar2 == 0)
               ) {
              lVar6 = 3;
              piVar7 = piVar4 + 0x18;
              goto LAB_ffff0000907c544c;
            }
            if (piVar4[0x20] == -0x6f86f7fc) {
              uVar1 = piVar4[0x21];
              uVar3 = FUN_ffff00009077f250(param_2);
              if ((uVar3 == uVar1) &&
                 (iVar2 = thunk_FUN_ffff00009077eae8
                                    (*(undefined8 *)(piVar4 + 0x24),param_2,(ulong)uVar1),
                 iVar2 == 0)) {
                lVar6 = 4;
                piVar7 = piVar4 + 0x20;
                goto LAB_ffff0000907c544c;
              }
              if (piVar4[0x28] == -0x6f86f7fc) {
                uVar1 = piVar4[0x29];
                uVar3 = FUN_ffff00009077f250(param_2);
                if ((uVar3 == uVar1) &&
                   (iVar2 = thunk_FUN_ffff00009077eae8
                                      (*(undefined8 *)(piVar4 + 0x2c),param_2,(ulong)uVar1),
                   iVar2 == 0)) {
                  lVar6 = 5;
                  piVar7 = piVar4 + 0x28;
                  goto LAB_ffff0000907c544c;
                }
                if (piVar4[0x30] == -0x6f86f7fc) {
                  uVar1 = piVar4[0x31];
                  uVar3 = FUN_ffff00009077f250(param_2);
                  if ((uVar3 == uVar1) &&
                     (iVar2 = thunk_FUN_ffff00009077eae8
                                        (*(undefined8 *)(piVar4 + 0x34),param_2,(ulong)uVar1),
                     iVar2 == 0)) {
                    lVar6 = 6;
                    piVar7 = piVar4 + 0x30;
                    goto LAB_ffff0000907c544c;
                  }
                  if (piVar4[0x38] == -0x6f86f7fc) {
                    uVar1 = piVar4[0x39];
                    uVar3 = FUN_ffff00009077f250(param_2);
                    if ((uVar3 == uVar1) &&
                       (iVar2 = thunk_FUN_ffff00009077eae8
                                          (*(undefined8 *)(piVar4 + 0x3c),param_2,(ulong)uVar1),
                       iVar2 == 0)) {
                      lVar6 = 7;
                      piVar7 = piVar4 + 0x38;
                      goto LAB_ffff0000907c544c;
                    }
                  }
                }
              }
            }
          }
        }
      }
    }
    uVar5 = 5;
  }
  return uVar5;
}


