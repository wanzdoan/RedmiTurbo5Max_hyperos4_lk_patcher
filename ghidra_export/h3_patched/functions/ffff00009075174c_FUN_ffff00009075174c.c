// ffff00009075174c  size=1208  FUN_ffff00009075174c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075174c(int param_1,undefined8 param_2)

{
  long *plVar1;
  undefined1 *puVar2;
  int *piVar3;
  ulong uVar4;
  int iVar5;
  undefined8 extraout_x1;
  undefined8 extraout_x1_00;
  undefined8 extraout_x1_01;
  undefined8 extraout_x1_02;
  undefined8 extraout_x1_03;
  undefined8 extraout_x1_04;
  undefined8 extraout_x1_05;
  undefined8 extraout_x1_06;
  undefined8 extraout_x1_07;
  undefined8 extraout_x1_08;
  undefined8 extraout_x1_09;
  uint uVar6;
  ulong uVar7;
  long *plVar8;
  long lVar9;
  long lVar10;
  long lVar11;
  int *piVar12;
  uint *puVar13;
  ulong uVar14;
  ulong uVar15;
  
  if ((DAT_ffff000090879374 & 1) == 0) {
    if (param_1 - 0x2bU < 2) {
      FUN_ffff0000907546d4(param_1);
      param_2 = extraout_x1;
    }
    DAT_ffff000090879374 = 1;
  }
  uVar6 = 0;
  if (param_1 != 0x2b) {
    uVar6 = (uint)(param_1 != 0x2d);
  }
  uVar14 = (ulong)uVar6;
  uVar4 = 1;
  if (param_1 != 0x2b) {
    uVar4 = 2;
  }
  lVar11 = uVar4 - uVar14;
  puVar2 = &DAT_ffff0000908781d8 + uVar14 * 0xbd8;
  piVar3 = (int *)(&DAT_ffff000090878738 + uVar14 * 0xbd8);
  plVar1 = (long *)(s_0123456789abcdef_ffff000090804a48 + uVar14 * 8 + 0x10);
  uVar15 = uVar14;
  do {
    uVar7 = uVar14;
    if (*(int *)(&DAT_ffff0000908781d8 + uVar15 * 0xbd8) == 0) {
      do {
        piVar12 = piVar3;
        lVar9 = lVar11;
        if (*(int *)(&DAT_ffff000090878738 + uVar7 * 0xbd8) == 0) {
          do {
            if (*piVar12 == 0) {
              FUN_ffff00009074e1bc(param_1,param_2,puVar2);
              param_2 = extraout_x1_06;
            }
            else {
              FUN_ffff00009074de5c(param_1,param_2,puVar2);
              param_2 = extraout_x1_07;
            }
            lVar9 = lVar9 + -1;
            piVar12 = piVar12 + 0x2f6;
          } while (lVar9 != 0);
        }
        else {
          do {
            if (*piVar12 == 0) {
              FUN_ffff00009074e1bc(param_1,param_2,&DAT_ffff0000908781d8);
              param_2 = extraout_x1_04;
            }
            else {
              FUN_ffff00009074de5c(param_1,param_2,&DAT_ffff0000908781d8);
              param_2 = extraout_x1_05;
            }
            piVar12 = piVar12 + 0x2f6;
            lVar9 = lVar9 + -1;
          } while (lVar9 != 0);
        }
        uVar7 = uVar7 + 1;
      } while (uVar7 != uVar4);
      FUN_ffff00009074cc4c(param_1);
      uVar7 = uVar14;
      if (DAT_ffff000090879370 == '\x01') {
        iVar5 = _DAT_ffff000090878298 << 1;
        if (_DAT_ffff00009087829c != 0) {
          iVar5 = _DAT_ffff00009087829c;
        }
        plVar8 = plVar1;
        lVar9 = lVar11;
        do {
          lVar10 = *plVar8;
          lVar9 = lVar9 + -1;
          *(uint *)(lVar10 + 0x34) = *(uint *)(lVar10 + 0x34) | 0x100000;
          *(uint *)(lVar10 + 0x200) =
               *(uint *)(lVar10 + 0x200) & 0xffff0000 | ((uint)(iVar5 * 1000) >> 0xd) + 1 & 0xffff;
          plVar8 = plVar8 + 1;
        } while (lVar9 != 0);
        FUN_ffff00009077de54
                  (s___s__set_DISP_REG_DSI_MODE_CON_ffff0000907e7ccb,s_DSI_SleepOut_ffff0000907de42b
                  );
      }
      else {
        DAT_ffff000090879370 = '\x01';
      }
      do {
        puVar13 = *(uint **)(s_0123456789abcdef_ffff000090804a48 + uVar7 * 8 + 0x10);
        *puVar13 = *puVar13 & 0xfffffffb;
        *puVar13 = *puVar13 | 4;
        FUN_ffff00009077cbd8(1000);
        uVar7 = uVar7 + 1;
        *puVar13 = *puVar13 & 0xfffffffb;
        puVar13[0xd] = puVar13[0xd] & 0xffefffff;
      } while (uVar4 != uVar7);
      FUN_ffff00009077de54
                (s___s__set_DISP_REG_DSI_MODE_CON_ffff0000907e7ccb,s_DSI_Wakeup_ffff0000907dcaf2);
      plVar8 = plVar1;
      lVar9 = lVar11;
      do {
        lVar10 = *plVar8;
        lVar9 = lVar9 + -1;
        *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) | 1;
        *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) & 0xfffffffe;
        param_2 = extraout_x1_09;
        plVar8 = plVar8 + 1;
      } while (lVar9 != 0);
    }
    else {
      do {
        piVar12 = piVar3;
        lVar9 = lVar11;
        if (*(int *)(&DAT_ffff000090878738 + uVar7 * 0xbd8) == 0) {
          do {
            if (*piVar12 == 0) {
              FUN_ffff00009074e1bc(param_1,param_2,puVar2);
              param_2 = extraout_x1_02;
            }
            else {
              FUN_ffff00009074de5c(param_1,param_2,puVar2);
              param_2 = extraout_x1_03;
            }
            lVar9 = lVar9 + -1;
            piVar12 = piVar12 + 0x2f6;
          } while (lVar9 != 0);
        }
        else {
          do {
            if (*piVar12 == 0) {
              FUN_ffff00009074e1bc(param_1,param_2,&DAT_ffff0000908781d8);
              param_2 = extraout_x1_00;
            }
            else {
              FUN_ffff00009074de5c(param_1,param_2,&DAT_ffff0000908781d8);
              param_2 = extraout_x1_01;
            }
            piVar12 = piVar12 + 0x2f6;
            lVar9 = lVar9 + -1;
          } while (lVar9 != 0);
        }
        uVar7 = uVar7 + 1;
      } while (uVar7 != uVar4);
      FUN_ffff00009074cc4c(param_1);
      uVar7 = uVar14;
      if (DAT_ffff000090879370 == '\x01') {
        iVar5 = _DAT_ffff000090878298 << 1;
        if (_DAT_ffff00009087829c != 0) {
          iVar5 = _DAT_ffff00009087829c;
        }
        plVar8 = plVar1;
        lVar9 = lVar11;
        do {
          lVar10 = *plVar8;
          lVar9 = lVar9 + -1;
          *(uint *)(lVar10 + 0x34) = *(uint *)(lVar10 + 0x34) | 0x100000;
          *(uint *)(lVar10 + 0x200) =
               *(uint *)(lVar10 + 0x200) & 0xffff0000 | ((uint)(iVar5 * 1000) >> 0xd) + 1 & 0xffff;
          plVar8 = plVar8 + 1;
        } while (lVar9 != 0);
        FUN_ffff00009077de54
                  (s___s__set_DISP_REG_DSI_MODE_CON_ffff0000907e7ccb,s_DSI_SleepOut_ffff0000907de42b
                  );
      }
      else {
        DAT_ffff000090879370 = '\x01';
      }
      do {
        puVar13 = *(uint **)(s_0123456789abcdef_ffff000090804a48 + uVar7 * 8 + 0x10);
        *puVar13 = *puVar13 & 0xfffffffb;
        *puVar13 = *puVar13 | 4;
        FUN_ffff00009077cbd8(1000);
        uVar7 = uVar7 + 1;
        *puVar13 = *puVar13 & 0xfffffffb;
        puVar13[0xd] = puVar13[0xd] & 0xffefffff;
      } while (uVar4 != uVar7);
      FUN_ffff00009077de54
                (s___s__set_DISP_REG_DSI_MODE_CON_ffff0000907e7ccb,s_DSI_Wakeup_ffff0000907dcaf2);
      uVar7 = uVar14;
      do {
        lVar9 = uVar7 * 8;
        uVar7 = uVar7 + 1;
        *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + lVar9 + 0x10) + 0x1d0) =
             *(uint *)(*(long *)(s_0123456789abcdef_ffff000090804a48 + lVar9 + 0x10) + 0x1d0) &
             0xfffffffe;
        plVar8 = plVar1;
        lVar9 = lVar11;
      } while (uVar4 != uVar7);
      do {
        lVar10 = *plVar8;
        lVar9 = lVar9 + -1;
        *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) | 1;
        *(uint *)(lVar10 + 0x30) = *(uint *)(lVar10 + 0x30) & 0xfffffffe;
        param_2 = extraout_x1_08;
        plVar8 = plVar8 + 1;
      } while (lVar9 != 0);
    }
    uVar15 = uVar15 + 1;
  } while (uVar15 != uVar4);
  return 0;
}


