
ulong FUN_ffff000090773214(long *param_1,uint param_2)

{
  int iVar1;
  int iVar2;
  undefined4 uVar3;
  char cVar4;
  uint uVar5;
  undefined1 uVar6;
  long lVar7;
  int *piVar8;
  int iVar9;
  undefined8 uVar10;
  long lVar11;
  uint uVar12;
  long lVar13;
  int iVar14;
  long lVar15;
  long *plVar16;
  ulong uVar17;
  ulong uVar18;
  ulong uVar19;
  ulong uVar20;
  
  if ((((param_1 == (long *)0x0) || (param_1[8] == 0)) || (param_1[9] == 0)) ||
     ((plVar16 = (long *)param_1[7], plVar16 == (long *)0x0 || ((long *)*plVar16 != param_1)))) {
    return 0xfffffffe;
  }
  iVar9 = (int)plVar16[1];
  if (((0x38 < iVar9 - 0x39U) || ((1L << ((ulong)(iVar9 - 0x39U) & 0x3f) & 0x100400400011001U) == 0)
      ) && ((iVar9 != 0x29a && (iVar9 != 0x2a)))) {
    return 0xfffffffe;
  }
  if (5 < param_2) {
    return 0xfffffffe;
  }
  if (((param_1[3] == 0) || ((lVar7 = param_1[1], (int)lVar7 != 0 && (*param_1 == 0)))) ||
     ((param_2 != 4 && (iVar9 == 0x29a)))) {
    param_1[6] = *(long *)(PTR_PTR_ffff000090839a08 + 0x20);
    return 0xfffffffe;
  }
  if ((int)param_1[4] == 0) {
LAB_ffff000090773678:
    param_1[6] = *(long *)(PTR_PTR_ffff000090839a08 + 0x38);
    return 0xfffffffb;
  }
  iVar2 = *(int *)((long)plVar16 + 0x4c);
  *(uint *)((long)plVar16 + 0x4c) = param_2;
  if (plVar16[5] == 0) {
    if ((int)lVar7 == 0) {
      iVar14 = -9;
      iVar1 = iVar14;
      if (iVar2 < 5) {
        iVar1 = 0;
      }
      lVar7 = 0;
      if (param_2 < 5) {
        iVar14 = 0;
      }
      if ((param_2 != 4) && ((int)(iVar14 + param_2 * 2) <= iVar1 + iVar2 * 2))
      goto LAB_ffff000090773678;
    }
    else {
      lVar7 = 0;
    }
  }
  else {
    FUN_ffff000090778f5c(plVar16);
    lVar7 = plVar16[5];
    uVar12 = *(uint *)(param_1 + 4);
    uVar5 = uVar12;
    if ((uint)lVar7 <= uVar12) {
      uVar5 = (uint)lVar7;
    }
    uVar17 = (ulong)uVar5;
    if (uVar5 != 0) {
      FUN_ffff00009077eb20(param_1[3],plVar16[4],uVar17);
      lVar11 = plVar16[4];
      lVar7 = plVar16[5];
      param_1[3] = param_1[3] + uVar17;
      uVar12 = (int)param_1[4] - uVar5;
      lVar7 = lVar7 - uVar17;
      param_1[5] = param_1[5] + uVar17;
      *(uint *)(param_1 + 4) = uVar12;
      plVar16[4] = lVar11 + uVar17;
      plVar16[5] = lVar7;
      if (lVar7 == 0) {
        plVar16[4] = plVar16[2];
      }
    }
    if (uVar12 == 0) goto LAB_ffff000090773ef4;
    iVar9 = (int)plVar16[1];
  }
  if (iVar9 == 0x2a) {
    if ((int)plVar16[6] != 0) {
      if (((int)plVar16[0x19] < 2) && (uVar5 = *(uint *)((long)plVar16 + 0xc4), 1 < (int)uVar5)) {
        if (uVar5 < 6) {
          uVar12 = 0x40;
        }
        else {
          uVar12 = 0x80;
          if (uVar5 != 6) {
            uVar12 = 0xc0;
          }
        }
      }
      else {
        uVar12 = 0;
      }
      uVar12 = uVar12 | *(int *)((long)plVar16 + 0x54) * 0x1000 - 0x7800U;
      if (*(int *)((long)plVar16 + 0xac) != 0) {
        uVar12 = uVar12 | 0x20;
      }
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)(uVar12 >> 8);
      lVar7 = plVar16[5];
      plVar16[5] = lVar7 + 1;
      *(byte *)(plVar16[2] + lVar7) =
           ((byte)uVar12 + (char)(uVar12 / 0x1f) * -0x1f | (byte)uVar12) ^ 0x1f;
      if (*(int *)((long)plVar16 + 0xac) != 0) {
        lVar7 = plVar16[5];
        lVar11 = param_1[0xc];
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 0x18);
        lVar7 = plVar16[5];
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 0x10);
        lVar7 = plVar16[5];
        lVar11 = param_1[0xc];
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 8);
        lVar7 = plVar16[5];
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)lVar11;
      }
      lVar7 = FUN_ffff00009076c2d0(0,0,0);
      param_1[0xc] = lVar7;
      *(undefined4 *)(plVar16 + 1) = 0x71;
      FUN_ffff000090774090(param_1);
      if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
      iVar9 = (int)plVar16[1];
      goto LAB_ffff0000907734f0;
    }
    *(undefined4 *)(plVar16 + 1) = 0x71;
code_r0xffff000090773db4:
    if ((int)param_1[1] == 0) goto LAB_ffff000090773dbc;
  }
  else {
    if (iVar9 != 0x29a) {
LAB_ffff0000907734f0:
      if (iVar9 == 0x39) {
        lVar7 = FUN_ffff00009076ca8c(0,0,0);
        lVar11 = plVar16[5];
        lVar13 = plVar16[2];
        param_1[0xc] = lVar7;
        plVar16[5] = lVar11 + 1;
        *(undefined1 *)(lVar13 + lVar11) = 0x1f;
        lVar7 = plVar16[5];
        plVar16[5] = lVar7 + 1;
        *(undefined1 *)(plVar16[2] + lVar7) = 0x8b;
        lVar7 = plVar16[5];
        plVar16[5] = lVar7 + 1;
        *(undefined1 *)(plVar16[2] + lVar7) = 8;
        piVar8 = (int *)plVar16[7];
        if (piVar8 == (int *)0x0) {
          lVar7 = plVar16[5];
          plVar16[5] = lVar7 + 1;
          *(undefined1 *)(plVar16[2] + lVar7) = 0;
          lVar7 = plVar16[5];
          plVar16[5] = lVar7 + 1;
          *(undefined1 *)(plVar16[2] + lVar7) = 0;
          lVar7 = plVar16[5];
          plVar16[5] = lVar7 + 1;
          *(undefined1 *)(plVar16[2] + lVar7) = 0;
          lVar7 = plVar16[5];
          plVar16[5] = lVar7 + 1;
          *(undefined1 *)(plVar16[2] + lVar7) = 0;
          lVar7 = plVar16[5];
          plVar16[5] = lVar7 + 1;
          *(undefined1 *)(plVar16[2] + lVar7) = 0;
          if (*(int *)((long)plVar16 + 0xc4) == 9) {
            uVar6 = 2;
          }
          else {
            uVar6 = 4;
            if (1 < *(int *)((long)plVar16 + 0xc4) && (int)plVar16[0x19] < 2) {
              uVar6 = 0;
            }
          }
          lVar7 = plVar16[5];
          plVar16[5] = lVar7 + 1;
          *(undefined1 *)(plVar16[2] + lVar7) = uVar6;
          lVar7 = plVar16[5];
          plVar16[5] = lVar7 + 1;
          *(undefined1 *)(plVar16[2] + lVar7) = 3;
          *(undefined4 *)(plVar16 + 1) = 0x71;
          FUN_ffff000090774090(param_1);
          if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
          iVar9 = (int)plVar16[1];
          goto LAB_ffff000090773838;
        }
        iVar9 = *piVar8;
        iVar2 = piVar8[0x11];
        lVar11 = *(long *)(piVar8 + 6);
        lVar13 = *(long *)(piVar8 + 10);
        lVar7 = *(long *)(piVar8 + 0xe);
        lVar15 = plVar16[5];
        plVar16[5] = lVar15 + 1;
        *(char *)(plVar16[2] + lVar15) =
             iVar9 != 0 | (iVar2 != 0) << 1 | (lVar11 != 0) << 2 | (lVar13 != 0) << 3 |
             (lVar7 != 0) << 4;
        lVar7 = plVar16[5];
        uVar10 = *(undefined8 *)(plVar16[7] + 8);
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)uVar10;
        lVar7 = plVar16[5];
        uVar10 = *(undefined8 *)(plVar16[7] + 8);
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)((ulong)uVar10 >> 8);
        lVar7 = plVar16[5];
        uVar10 = *(undefined8 *)(plVar16[7] + 8);
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)((ulong)uVar10 >> 0x10);
        lVar7 = plVar16[5];
        uVar10 = *(undefined8 *)(plVar16[7] + 8);
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)((ulong)uVar10 >> 0x18);
        if (*(int *)((long)plVar16 + 0xc4) == 9) {
          uVar6 = 2;
        }
        else {
          uVar6 = 4;
          if (1 < *(int *)((long)plVar16 + 0xc4) && (int)plVar16[0x19] < 2) {
            uVar6 = 0;
          }
        }
        lVar7 = plVar16[5];
        plVar16[5] = lVar7 + 1;
        *(undefined1 *)(plVar16[2] + lVar7) = uVar6;
        lVar7 = plVar16[5];
        uVar3 = *(undefined4 *)(plVar16[7] + 0x14);
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)uVar3;
        lVar7 = plVar16[7];
        if (*(long *)(lVar7 + 0x18) != 0) {
          lVar11 = plVar16[5];
          uVar3 = *(undefined4 *)(lVar7 + 0x20);
          plVar16[5] = lVar11 + 1;
          *(char *)(plVar16[2] + lVar11) = (char)uVar3;
          lVar7 = plVar16[5];
          uVar3 = *(undefined4 *)(plVar16[7] + 0x20);
          plVar16[5] = lVar7 + 1;
          *(char *)(plVar16[2] + lVar7) = (char)((uint)uVar3 >> 8);
          lVar7 = plVar16[7];
        }
        if (*(int *)(lVar7 + 0x44) != 0) {
          lVar7 = FUN_ffff00009076ca8c(param_1[0xc],plVar16[2],(int)plVar16[5]);
          param_1[0xc] = lVar7;
        }
        plVar16[8] = 0;
        *(undefined4 *)(plVar16 + 1) = 0x45;
code_r0xffff000090773860:
        lVar7 = *(long *)(plVar16[7] + 0x18);
        if (lVar7 != 0) {
          lVar11 = plVar16[8];
          uVar19 = plVar16[5];
          uVar5 = (uint)*(ushort *)(plVar16[7] + 0x20) - (int)lVar11;
          uVar17 = (ulong)uVar5;
          if ((ulong)plVar16[3] < uVar19 + uVar17) {
            uVar20 = plVar16[3] - uVar19;
            uVar18 = uVar20 & 0xffffffff;
            FUN_ffff00009077eb20(plVar16[2] + uVar19,lVar7 + lVar11,uVar18);
            uVar17 = plVar16[3];
            iVar9 = *(int *)(plVar16[7] + 0x44);
            plVar16[5] = uVar17;
            if ((iVar9 != 0) && (uVar19 < uVar17)) {
              lVar7 = FUN_ffff00009076ca8c
                                (param_1[0xc],plVar16[2] + uVar19,(int)uVar17 - (int)uVar19);
              param_1[0xc] = lVar7;
            }
            lVar7 = param_1[7];
            plVar16[8] = plVar16[8] + uVar18;
            FUN_ffff000090778f5c(lVar7);
            uVar12 = *(uint *)(param_1 + 4);
            if (*(uint *)(lVar7 + 0x28) <= *(uint *)(param_1 + 4)) {
              uVar12 = *(uint *)(lVar7 + 0x28);
            }
            uVar17 = (ulong)uVar12;
            if (uVar12 != 0) {
              FUN_ffff00009077eb20(param_1[3],*(undefined8 *)(lVar7 + 0x20),uVar17);
              lVar11 = *(long *)(lVar7 + 0x20);
              lVar13 = *(long *)(lVar7 + 0x28);
              param_1[3] = param_1[3] + uVar17;
              lVar13 = lVar13 - uVar17;
              param_1[5] = param_1[5] + uVar17;
              *(uint *)(param_1 + 4) = (int)param_1[4] - uVar12;
              *(ulong *)(lVar7 + 0x20) = lVar11 + uVar17;
              *(long *)(lVar7 + 0x28) = lVar13;
              if (lVar13 == 0) {
                *(undefined8 *)(lVar7 + 0x20) = *(undefined8 *)(lVar7 + 0x10);
              }
            }
            if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
            uVar19 = plVar16[3];
            uVar17 = (ulong)(uVar5 - (int)uVar20);
            if (uVar19 < uVar17) {
              do {
                FUN_ffff00009077eb20
                          (plVar16[2],*(long *)(plVar16[7] + 0x18) + plVar16[8],uVar19 & 0xffffffff)
                ;
                iVar9 = *(int *)(plVar16[7] + 0x44);
                plVar16[5] = plVar16[3];
                if ((iVar9 != 0) && (plVar16[3] != 0)) {
                  lVar7 = FUN_ffff00009076ca8c(param_1[0xc],plVar16[2]);
                  param_1[0xc] = lVar7;
                }
                lVar7 = param_1[7];
                plVar16[8] = plVar16[8] + (uVar19 & 0xffffffff);
                FUN_ffff000090778f5c(lVar7);
                uVar5 = *(uint *)(param_1 + 4);
                if (*(uint *)(lVar7 + 0x28) <= *(uint *)(param_1 + 4)) {
                  uVar5 = *(uint *)(lVar7 + 0x28);
                }
                uVar18 = (ulong)uVar5;
                if (uVar5 != 0) {
                  FUN_ffff00009077eb20(param_1[3],*(undefined8 *)(lVar7 + 0x20),uVar18);
                  lVar11 = *(long *)(lVar7 + 0x20);
                  lVar13 = *(long *)(lVar7 + 0x28);
                  param_1[3] = param_1[3] + uVar18;
                  lVar13 = lVar13 - uVar18;
                  param_1[5] = param_1[5] + uVar18;
                  *(uint *)(param_1 + 4) = (int)param_1[4] - uVar5;
                  *(ulong *)(lVar7 + 0x20) = lVar11 + uVar18;
                  *(long *)(lVar7 + 0x28) = lVar13;
                  if (lVar13 == 0) {
                    *(undefined8 *)(lVar7 + 0x20) = *(undefined8 *)(lVar7 + 0x10);
                  }
                }
                if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
                uVar17 = (ulong)(uint)((int)uVar17 - (int)uVar19);
                uVar19 = plVar16[3];
              } while (uVar19 < uVar17);
            }
            lVar11 = plVar16[8];
            uVar19 = 0;
            lVar7 = *(long *)(plVar16[7] + 0x18);
          }
          FUN_ffff00009077eb20(plVar16[2] + uVar19,lVar7 + lVar11,uVar17);
          uVar17 = plVar16[5] + uVar17;
          iVar9 = *(int *)(plVar16[7] + 0x44);
          plVar16[5] = uVar17;
          if ((iVar9 != 0) && (uVar19 < uVar17)) {
            lVar7 = FUN_ffff00009076ca8c(param_1[0xc],plVar16[2] + uVar19,(int)uVar17 - (int)uVar19)
            ;
            param_1[0xc] = lVar7;
          }
          plVar16[8] = 0;
        }
        *(undefined4 *)(plVar16 + 1) = 0x49;
code_r0xffff000090773ab8:
        if (*(long *)(plVar16[7] + 0x28) != 0) {
          uVar17 = plVar16[5];
          do {
            uVar19 = plVar16[5];
            if (uVar19 == plVar16[3]) {
              if (*(int *)(plVar16[7] + 0x44) != 0 && uVar17 < uVar19) {
                lVar7 = FUN_ffff00009076ca8c
                                  (param_1[0xc],plVar16[2] + uVar17,(int)uVar19 - (int)uVar17);
                param_1[0xc] = lVar7;
              }
              lVar7 = param_1[7];
              FUN_ffff000090778f5c(lVar7);
              uVar5 = *(uint *)(param_1 + 4);
              if (*(uint *)(lVar7 + 0x28) <= *(uint *)(param_1 + 4)) {
                uVar5 = *(uint *)(lVar7 + 0x28);
              }
              uVar17 = (ulong)uVar5;
              if (uVar5 != 0) {
                FUN_ffff00009077eb20(param_1[3],*(undefined8 *)(lVar7 + 0x20),uVar17);
                lVar11 = *(long *)(lVar7 + 0x20);
                lVar13 = *(long *)(lVar7 + 0x28);
                param_1[3] = param_1[3] + uVar17;
                lVar13 = lVar13 - uVar17;
                param_1[5] = param_1[5] + uVar17;
                *(uint *)(param_1 + 4) = (int)param_1[4] - uVar5;
                *(ulong *)(lVar7 + 0x20) = lVar11 + uVar17;
                *(long *)(lVar7 + 0x28) = lVar13;
                if (lVar13 == 0) {
                  *(undefined8 *)(lVar7 + 0x20) = *(undefined8 *)(lVar7 + 0x10);
                }
              }
              if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
              uVar19 = 0;
              uVar17 = 0;
            }
            lVar7 = plVar16[8];
            lVar11 = *(long *)(plVar16[7] + 0x28);
            plVar16[8] = lVar7 + 1;
            cVar4 = *(char *)(lVar11 + lVar7);
            plVar16[5] = uVar19 + 1;
            *(char *)(plVar16[2] + uVar19) = cVar4;
          } while (cVar4 != '\0');
          if ((*(int *)(plVar16[7] + 0x44) != 0) && (uVar17 < (ulong)plVar16[5])) {
            lVar7 = FUN_ffff00009076ca8c
                              (param_1[0xc],plVar16[2] + uVar17,(int)plVar16[5] - (int)uVar17);
            param_1[0xc] = lVar7;
          }
          plVar16[8] = 0;
        }
        *(undefined4 *)(plVar16 + 1) = 0x5b;
        goto code_r0xffff000090773bf0;
      }
LAB_ffff000090773838:
      switch(iVar9) {
      case 0x45:
        goto code_r0xffff000090773860;
      default:
        goto code_r0xffff000090773db4;
      case 0x49:
        goto code_r0xffff000090773ab8;
      case 0x5b:
code_r0xffff000090773bf0:
        if (*(long *)(plVar16[7] + 0x38) != 0) {
          uVar17 = plVar16[5];
          do {
            uVar19 = plVar16[5];
            if (uVar19 == plVar16[3]) {
              if (*(int *)(plVar16[7] + 0x44) != 0 && uVar17 < uVar19) {
                lVar7 = FUN_ffff00009076ca8c
                                  (param_1[0xc],plVar16[2] + uVar17,(int)uVar19 - (int)uVar17);
                param_1[0xc] = lVar7;
              }
              lVar7 = param_1[7];
              FUN_ffff000090778f5c(lVar7);
              uVar5 = *(uint *)(param_1 + 4);
              if (*(uint *)(lVar7 + 0x28) <= *(uint *)(param_1 + 4)) {
                uVar5 = *(uint *)(lVar7 + 0x28);
              }
              uVar17 = (ulong)uVar5;
              if (uVar5 != 0) {
                FUN_ffff00009077eb20(param_1[3],*(undefined8 *)(lVar7 + 0x20),uVar17);
                lVar11 = *(long *)(lVar7 + 0x20);
                lVar13 = *(long *)(lVar7 + 0x28);
                param_1[3] = param_1[3] + uVar17;
                lVar13 = lVar13 - uVar17;
                param_1[5] = param_1[5] + uVar17;
                *(uint *)(param_1 + 4) = (int)param_1[4] - uVar5;
                *(ulong *)(lVar7 + 0x20) = lVar11 + uVar17;
                *(long *)(lVar7 + 0x28) = lVar13;
                if (lVar13 == 0) {
                  *(undefined8 *)(lVar7 + 0x20) = *(undefined8 *)(lVar7 + 0x10);
                }
              }
              if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
              uVar19 = 0;
              uVar17 = 0;
            }
            lVar7 = plVar16[8];
            lVar11 = *(long *)(plVar16[7] + 0x38);
            plVar16[8] = lVar7 + 1;
            cVar4 = *(char *)(lVar11 + lVar7);
            plVar16[5] = uVar19 + 1;
            *(char *)(plVar16[2] + uVar19) = cVar4;
          } while (cVar4 != '\0');
          if ((*(int *)(plVar16[7] + 0x44) != 0) && (uVar17 < (ulong)plVar16[5])) {
            lVar7 = FUN_ffff00009076ca8c
                              (param_1[0xc],plVar16[2] + uVar17,(int)plVar16[5] - (int)uVar17);
            param_1[0xc] = lVar7;
          }
        }
        *(undefined4 *)(plVar16 + 1) = 0x67;
        break;
      case 0x67:
        break;
      }
      if (*(int *)(plVar16[7] + 0x44) != 0) {
        lVar7 = plVar16[5];
        if ((ulong)plVar16[3] < lVar7 + 2U) {
          FUN_ffff000090774090(param_1);
          lVar7 = 0;
          if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
        }
        lVar11 = param_1[0xc];
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)lVar11;
        lVar7 = plVar16[5];
        lVar11 = param_1[0xc];
        plVar16[5] = lVar7 + 1;
        *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 8);
        lVar7 = FUN_ffff00009076ca8c(0,0,0);
        param_1[0xc] = lVar7;
      }
      *(undefined4 *)(plVar16 + 1) = 0x71;
      FUN_ffff000090774090(param_1);
      if (plVar16[5] != 0) goto LAB_ffff000090773ef4;
      goto code_r0xffff000090773db4;
    }
    if ((int)param_1[1] != 0) goto LAB_ffff000090773678;
LAB_ffff000090773dbc:
    if (*(int *)((long)plVar16 + 0xb4) == 0) {
      if (param_2 == 0) {
        return 0;
      }
      if ((int)plVar16[1] == 0x29a) goto LAB_ffff000090773ee4;
    }
  }
  if (*(int *)((long)plVar16 + 0xc4) == 0) {
    uVar5 = FUN_ffff00009077412c(plVar16,param_2);
  }
  else if ((int)plVar16[0x19] == 3) {
    uVar5 = FUN_ffff000090774b04(plVar16,param_2);
  }
  else if ((int)plVar16[0x19] == 2) {
    uVar5 = FUN_ffff0000907747e0(plVar16,param_2);
  }
  else {
    uVar5 = (*(code *)(&PTR_FUN_ffff0000908326f8)[(long)*(int *)((long)plVar16 + 0xc4) * 2])
                      (plVar16,param_2);
  }
  if ((uVar5 & 0xfffffffe) == 2) {
    *(undefined4 *)(plVar16 + 1) = 0x29a;
  }
  if ((uVar5 & 0xfffffffd) != 0) {
    if (uVar5 == 1) {
      if (param_2 != 5) {
        if (param_2 == 1) {
          FUN_ffff000090778ff4();
        }
        else {
          FUN_ffff000090778dd4(plVar16,0,0,0);
          if (param_2 == 3) {
            lVar7 = plVar16[0xf];
            *(undefined2 *)(lVar7 + (ulong)(*(int *)((long)plVar16 + 0x84) - 1) * 2) = 0;
            FUN_ffff00009077ee50(lVar7,0);
            if (*(int *)((long)plVar16 + 0xb4) == 0) {
              *(undefined4 *)((long)plVar16 + 0xac) = 0;
              plVar16[0x13] = 0;
              *(undefined4 *)((long)plVar16 + 0x172c) = 0;
            }
          }
        }
      }
      FUN_ffff000090774090(param_1);
      if ((int)param_1[4] == 0) goto LAB_ffff000090773ef4;
    }
LAB_ffff000090773ee4:
    if (param_2 != 4) {
      return 0;
    }
    if ((int)plVar16[6] < 1) {
      return 1;
    }
    lVar7 = param_1[0xc];
    if ((int)plVar16[6] == 2) {
      lVar11 = plVar16[5];
      plVar16[5] = lVar11 + 1;
      *(char *)(plVar16[2] + lVar11) = (char)lVar7;
      lVar7 = plVar16[5];
      lVar11 = param_1[0xc];
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 8);
      lVar7 = plVar16[5];
      lVar11 = param_1[0xc];
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 0x10);
      lVar7 = plVar16[5];
      lVar11 = param_1[0xc];
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 0x18);
      lVar7 = plVar16[5];
      lVar11 = param_1[2];
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)lVar11;
      lVar7 = plVar16[5];
      lVar11 = param_1[2];
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 8);
      lVar7 = plVar16[5];
      lVar11 = param_1[2];
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 0x10);
      lVar7 = plVar16[5];
      lVar11 = plVar16[2];
      uVar6 = (undefined1)((ulong)param_1[2] >> 0x18);
    }
    else {
      lVar11 = plVar16[5];
      plVar16[5] = lVar11 + 1;
      *(char *)(plVar16[2] + lVar11) = (char)((ulong)lVar7 >> 0x18);
      lVar11 = plVar16[5];
      plVar16[5] = lVar11 + 1;
      *(char *)(plVar16[2] + lVar11) = (char)((ulong)lVar7 >> 0x10);
      lVar7 = plVar16[5];
      lVar11 = param_1[0xc];
      uVar6 = (undefined1)lVar11;
      plVar16[5] = lVar7 + 1;
      *(char *)(plVar16[2] + lVar7) = (char)((ulong)lVar11 >> 8);
      lVar7 = plVar16[5];
      lVar11 = plVar16[2];
    }
    plVar16[5] = lVar7 + 1;
    *(undefined1 *)(lVar11 + lVar7) = uVar6;
    FUN_ffff000090774090(param_1);
    if (0 < (int)plVar16[6]) {
      *(int *)(plVar16 + 6) = -(int)plVar16[6];
    }
    return (ulong)(plVar16[5] == 0);
  }
  if ((int)param_1[4] != 0) {
    return 0;
  }
LAB_ffff000090773ef4:
  *(undefined4 *)((long)plVar16 + 0x4c) = 0xffffffff;
  return 0;
}


