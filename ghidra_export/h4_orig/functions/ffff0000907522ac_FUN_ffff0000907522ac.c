// ffff0000907522ac  size=648  FUN_ffff0000907522ac

undefined8 FUN_ffff0000907522ac(int param_1,long param_2)

{
  undefined1 *puVar1;
  ulong uVar2;
  long lVar3;
  long extraout_x1;
  long extraout_x1_00;
  long extraout_x1_01;
  long extraout_x1_02;
  long extraout_x1_03;
  long extraout_x1_04;
  long extraout_x1_05;
  long extraout_x1_06;
  uint uVar4;
  long *plVar5;
  ulong uVar6;
  int *piVar7;
  long lVar8;
  ulong uVar9;
  long lVar10;
  long lVar11;
  
  if (DAT_ffff000090879410 == '\x01') {
    if (*(int *)(param_2 + 0x198) == 2) {
      if (param_1 == 0x2b) {
        param_1 = 0x2d;
      }
      else if (param_1 == 0x2c) {
        return 0;
      }
    }
    uVar4 = 0;
    if (param_1 != 0x2b) {
      uVar4 = (uint)(param_1 != 0x2d);
    }
    uVar6 = (ulong)uVar4;
    uVar2 = 1;
    if (param_1 != 0x2b) {
      uVar2 = 2;
    }
    puVar1 = &DAT_ffff000090878270 + uVar6 * 0xbd8;
    lVar3 = uVar6 * 0xbd8;
    if (*(int *)(param_2 + 0x170) == 0) {
      lVar8 = uVar2 - uVar6;
      lVar11 = param_2;
      do {
        piVar7 = (int *)(&DAT_ffff0000908787d0 + lVar3);
        lVar10 = lVar8;
        if (*(int *)(&DAT_ffff0000908787d0 + uVar6 * 0xbd8) == 0) {
          do {
            if (*piVar7 == 0) {
              FUN_ffff00009074e454(param_1,lVar11,puVar1);
              lVar11 = extraout_x1_05;
            }
            else {
              FUN_ffff00009074e0f4(param_1,lVar11,puVar1);
              lVar11 = extraout_x1_06;
            }
            lVar10 = lVar10 + -1;
            piVar7 = piVar7 + 0x2f6;
          } while (lVar10 != 0);
        }
        else {
          do {
            if (*piVar7 == 0) {
              FUN_ffff00009074e454(param_1,lVar11,&DAT_ffff000090878270);
              lVar11 = extraout_x1_03;
            }
            else {
              FUN_ffff00009074e0f4(param_1,lVar11,&DAT_ffff000090878270);
              lVar11 = extraout_x1_04;
            }
            piVar7 = piVar7 + 0x2f6;
            lVar10 = lVar10 + -1;
          } while (lVar10 != 0);
        }
        uVar6 = uVar6 + 1;
      } while (uVar6 != uVar2);
      FUN_ffff00009074cee4(param_1);
      FUN_ffff00009074cabc(param_1,param_2);
    }
    else {
      lVar11 = uVar2 - uVar6;
      lVar8 = param_2;
      uVar9 = uVar6;
      do {
        piVar7 = (int *)(&DAT_ffff0000908787d0 + lVar3);
        lVar10 = lVar11;
        if (*(int *)(&DAT_ffff0000908787d0 + uVar9 * 0xbd8) == 0) {
          do {
            if (*piVar7 == 0) {
              FUN_ffff00009074e454(param_1,lVar8,puVar1);
              lVar8 = extraout_x1_01;
            }
            else {
              FUN_ffff00009074e0f4(param_1,lVar8,puVar1);
              lVar8 = extraout_x1_02;
            }
            lVar10 = lVar10 + -1;
            piVar7 = piVar7 + 0x2f6;
          } while (lVar10 != 0);
        }
        else {
          do {
            if (*piVar7 == 0) {
              FUN_ffff00009074e454(param_1,lVar8,&DAT_ffff000090878270);
              lVar8 = extraout_x1;
            }
            else {
              FUN_ffff00009074e0f4(param_1,lVar8,&DAT_ffff000090878270);
              lVar8 = extraout_x1_00;
            }
            piVar7 = piVar7 + 0x2f6;
            lVar10 = lVar10 + -1;
          } while (lVar10 != 0);
        }
        uVar9 = uVar9 + 1;
      } while (uVar9 != uVar2);
      FUN_ffff00009074cee4(param_1);
      FUN_ffff00009074cabc(param_1,param_2);
      plVar5 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar6 * 8 + 0x10);
      do {
        lVar11 = lVar11 + -1;
        *(uint *)(*plVar5 + 0x1d0) = *(uint *)(*plVar5 + 0x1d0) & 0xfffffffe;
        plVar5 = plVar5 + 1;
      } while (lVar11 != 0);
    }
    DAT_ffff000090879410 = '\0';
  }
  return 0;
}


