
undefined8 FUN_ffff0000be021228(long param_1)

{
  uint *puVar1;
  uint uVar2;
  uint uVar3;
  uint uVar4;
  byte bVar5;
  int iVar6;
  undefined8 *puVar7;
  long lVar8;
  long lVar9;
  long lVar10;
  uint *puVar11;
  uint *puVar12;
  long *plVar13;
  ulong *puVar14;
  undefined8 *puVar15;
  uint uVar16;
  ulong uVar17;
  uint local_44;
  
  FUN_ffff0000be020ec8(&DAT_ffff0000be117870);
  puVar7 = (undefined8 *)FUN_ffff0000be05f1bc(0x40);
  if (puVar7 != (undefined8 *)0x0) {
    local_44 = 0;
    if (param_1 == 0) {
      iVar6 = FUN_ffff0000be021218(puVar7);
      if (iVar6 != 0) {
        uVar16 = 0;
LAB_ffff0000be021404:
        bVar5 = DAT_ffff0000be117870;
        *puVar7 = 0x5005000012345678;
        *(undefined8 *)((long)puVar7 + 0xc) = 0xea00000002;
        *(uint *)(puVar7 + 1) = uVar16;
        *(undefined4 *)((long)puVar7 + 0x2c) = 0x12345678;
        *(undefined4 *)((long)puVar7 + 0x14) = 0x4d;
        *(uint *)(puVar7 + 3) = (uint)bVar5;
        DAT_ffff0000be117a68 = puVar7;
        return 0;
      }
    }
    else {
      iVar6 = FUN_ffff0000be05620c(param_1,s__soc_emicen_ffff0000be0c2b0e);
      if ((((-1 < iVar6) ||
           (iVar6 = FUN_ffff0000be05620c(param_1,s__emicen_ffff0000be0c3f10), -1 < iVar6)) ||
          (iVar6 = FUN_ffff0000be05620c(param_1,0), -1 < iVar6)) &&
         ((lVar8 = FUN_ffff0000be056494(param_1,iVar6,&DAT_ffff0000be0ae6fc,&local_44),
          uVar3 = local_44, lVar8 != 0 && (local_44 != 0)))) {
        uVar16 = (int)local_44 >> 4;
        lVar9 = FUN_ffff0000be05f1bc((ulong)uVar16 << 4);
        puVar7[6] = lVar9;
        if (lVar9 != 0) {
          if (0xf < uVar3) {
            uVar17 = 0;
            uVar2 = uVar16;
            if (uVar16 < 2) {
              uVar2 = 1;
            }
            plVar13 = (long *)(lVar9 + 8);
            do {
              lVar10 = FUN_ffff0000be05f1bc(0x10);
              *plVar13 = lVar10;
              if (lVar10 == 0) {
                if (uVar17 != 0) {
                  puVar15 = (undefined8 *)(lVar9 + 8);
                  do {
                    thunk_FUN_ffff0000be09937c(*puVar15);
                    uVar17 = uVar17 - 1;
                    puVar15 = puVar15 + 2;
                  } while (uVar17 != 0);
                }
                goto LAB_ffff0000be021490;
              }
              uVar17 = uVar17 + 1;
              plVar13 = plVar13 + 2;
            } while (uVar2 != uVar17);
          }
          iVar6 = FUN_ffff0000be05620c(param_1,s__soc_emichn_ffff0000be0bd044);
          if ((((-1 < iVar6) ||
               (iVar6 = FUN_ffff0000be05620c(param_1,s__emichn_ffff0000be0b3ff9), -1 < iVar6)) ||
              (iVar6 = FUN_ffff0000be05620c(param_1,0), -1 < iVar6)) &&
             ((lVar10 = FUN_ffff0000be056494(param_1,iVar6,&DAT_ffff0000be0ae6fc,&local_44),
              lVar10 != 0 && (local_44 != 0)))) {
            *(uint *)(puVar7 + 7) = (uint)DAT_ffff0000be117872;
            if (0xf < uVar3) {
              puVar11 = (uint *)(lVar10 + 4);
              uVar3 = uVar16;
              if (uVar16 < 2) {
                uVar3 = 1;
              }
              uVar17 = (ulong)uVar3;
              puVar12 = (uint *)(lVar8 + 4);
              plVar13 = (long *)(lVar9 + 8);
              do {
                uVar3 = *puVar11;
                uVar17 = uVar17 - 1;
                puVar1 = puVar11 + 4;
                puVar11 = puVar11 + 8;
                uVar2 = (*puVar12 & 0xff00ff00) >> 8 | (*puVar12 & 0xff00ff) << 8;
                uVar3 = (uVar3 & 0xff00ff00) >> 8 | (uVar3 & 0xff00ff) << 8;
                uVar4 = (*puVar1 & 0xff00ff00) >> 8 | (*puVar1 & 0xff00ff) << 8;
                plVar13[-1] = (ulong)(uVar2 >> 0x10 | uVar2 << 0x10) | 0xffff000000000000;
                puVar14 = (ulong *)*plVar13;
                *puVar14 = (ulong)(uVar3 >> 0x10 | uVar3 << 0x10) | 0xffff000000000000;
                puVar14[1] = (ulong)(uVar4 >> 0x10 | uVar4 << 0x10) | 0xffff000000000000;
                puVar12 = puVar12 + 4;
                plVar13 = plVar13 + 2;
              } while (uVar17 != 0);
            }
            goto LAB_ffff0000be021404;
          }
          if (0xf < uVar3) {
            if (uVar16 < 2) {
              uVar16 = 1;
            }
            uVar17 = (ulong)uVar16;
            puVar15 = (undefined8 *)(lVar9 + 8);
            do {
              thunk_FUN_ffff0000be09937c(*puVar15);
              uVar17 = uVar17 - 1;
              puVar15 = puVar15 + 2;
            } while (uVar17 != 0);
          }
LAB_ffff0000be021490:
          thunk_FUN_ffff0000be09937c(lVar9);
        }
      }
    }
    thunk_FUN_ffff0000be09937c(puVar7);
  }
  return 0xffffffff;
}


