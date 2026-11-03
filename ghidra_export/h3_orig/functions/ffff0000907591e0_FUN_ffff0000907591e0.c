// ffff0000907591e0  size=592  FUN_ffff0000907591e0

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff0000907591e0(long param_1)

{
  int iVar1;
  int iVar2;
  undefined *puVar3;
  ulong uVar4;
  uint uVar5;
  ulong uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  long lVar11;
  ulong uVar12;
  uint uVar13;
  
  puVar3 = PTR_DAT_ffff0000908399a0;
  uVar8 = 0;
  do {
    if (*(int *)(param_1 + uVar8 * 4) == -1) {
      if ((uint)uVar8 < 2) goto LAB_ffff0000907593dc;
      break;
    }
    uVar8 = uVar8 + 1;
  } while (uVar8 != 0x4b);
  uVar7 = 0;
  uVar10 = 0;
  uVar9 = 1;
  do {
    iVar1 = *(int *)(param_1 + uVar7 * 4);
    uVar12 = 0;
    uVar13 = 0;
    do {
      uVar4 = uVar9;
      if (iVar1 == (&DAT_ffff000090820b50)[uVar12 * 0x1a]) {
        do {
          uVar6 = uVar4;
          iVar2 = *(int *)(param_1 + uVar6 * 4);
          uVar4 = uVar6 + 1;
        } while (*(int *)(puVar3 + (long)iVar2 * 0x20 + 0x10) == 0 && uVar6 < (uVar8 & 0xffffffff));
        if ((uVar8 & 0xffffffff) <= uVar6) {
                    /* WARNING: Subroutine does not return */
          FUN_ffff00009077cd04
                    (s_platform_mediatek_mt6991_disp_dd_ffff0000907dcda8,0x597,
                     s_step_<_module_num_ffff0000907d4cb3);
        }
        uVar5 = (&DAT_ffff000090820ba0)[uVar12 * 0x1a];
        if ((&DAT_ffff000090820b54)[uVar12 * 0x1a] != -1) {
          if ((&DAT_ffff000090820b54)[uVar12 * 0x1a] == iVar2) {
            lVar11 = 0;
          }
          else {
            if ((&DAT_ffff000090820b5c)[uVar12 * 0x1a] == -1) goto LAB_ffff00009075927c;
            if ((&DAT_ffff000090820b5c)[uVar12 * 0x1a] == iVar2) {
              lVar11 = 1;
            }
            else {
              if ((&DAT_ffff000090820b64)[uVar12 * 0x1a] == -1) goto LAB_ffff00009075927c;
              if ((&DAT_ffff000090820b64)[uVar12 * 0x1a] == iVar2) {
                lVar11 = 2;
              }
              else {
                if ((&DAT_ffff000090820b6c)[uVar12 * 0x1a] == -1) goto LAB_ffff00009075927c;
                if ((&DAT_ffff000090820b6c)[uVar12 * 0x1a] == iVar2) {
                  lVar11 = 3;
                }
                else {
                  if ((&DAT_ffff000090820b74)[uVar12 * 0x1a] == -1) goto LAB_ffff00009075927c;
                  if ((&DAT_ffff000090820b74)[uVar12 * 0x1a] == iVar2) {
                    lVar11 = 4;
                  }
                  else {
                    if ((&DAT_ffff000090820b7c)[uVar12 * 0x1a] == -1) goto LAB_ffff00009075927c;
                    if ((&DAT_ffff000090820b7c)[uVar12 * 0x1a] == iVar2) {
                      lVar11 = 5;
                    }
                    else {
                      if ((&DAT_ffff000090820b84)[uVar12 * 0x1a] == -1) goto LAB_ffff00009075927c;
                      if ((&DAT_ffff000090820b84)[uVar12 * 0x1a] == iVar2) {
                        lVar11 = 6;
                      }
                      else {
                        if (((&DAT_ffff000090820b8c)[uVar12 * 0x1a] == -1) ||
                           ((&DAT_ffff000090820b8c)[uVar12 * 0x1a] != iVar2))
                        goto LAB_ffff00009075927c;
                        lVar11 = 7;
                      }
                    }
                  }
                }
              }
            }
          }
          uVar5 = (&DAT_ffff000090820b58)[lVar11 * 2 + uVar12 * 0x1a] | uVar5;
          uVar10 = uVar12 & 0xffffffff;
          uVar13 = uVar5 | uVar13;
        }
LAB_ffff00009075927c:
        (&DAT_ffff000090820ba0)[uVar12 * 0x1a] = uVar5;
      }
      uVar12 = uVar12 + 1;
    } while (uVar12 != 0x1b);
    if (uVar13 != 0) {
      lVar11 = uVar10 * 0xd;
      uVar10 = 0;
      *(uint *)(&DAT_ffff000090820b98)[lVar11] = uVar13;
    }
    uVar7 = uVar7 + 1;
    uVar9 = uVar9 + 1;
  } while (uVar7 != (int)uVar8 - 1);
LAB_ffff0000907593dc:
  _DAT_ffff000032000dd0 = _DAT_ffff000032000dd0 | 4;
  _DAT_ffff000032000e3c = _DAT_ffff000032000e3c | 0x400;
  _DAT_ffff000032000e54 = _DAT_ffff000032000e54 | 0x800;
  return;
}


