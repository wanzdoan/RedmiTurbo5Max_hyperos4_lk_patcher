
/* WARNING: Type propagation algorithm not settling */

uint FUN_ffff0000b8030cc4(int param_1,undefined8 param_2,byte param_3,long param_4,byte param_5)

{
  uint uVar1;
  ulong uVar2;
  uint uVar3;
  uint uVar4;
  undefined8 *puVar5;
  uint uVar6;
  long lVar7;
  long lVar8;
  ulong uVar9;
  long *plVar10;
  uint uVar11;
  int iVar12;
  undefined8 *puVar13;
  uint *puVar14;
  ulong uVar15;
  ulong uVar16;
  int iVar17;
  undefined4 *puVar18;
  long lVar19;
  undefined1 local_70 [8];
  undefined8 local_68;
  
  uVar1 = (uint)param_5;
  uVar6 = 0;
  if (param_1 != 0x2b) {
    uVar6 = (uint)(param_1 != 0x2d);
  }
  uVar15 = (ulong)uVar6;
  uVar2 = 1;
  if (param_1 != 0x2b) {
    uVar2 = 2;
  }
  uVar6 = 0x604;
  if (0xaf < param_3) {
    uVar6 = 0x1404;
  }
  uVar3 = uVar1;
  if (9 < uVar1) {
    uVar3 = 10;
  }
  lVar19 = uVar2 - uVar15;
  iVar17 = 5;
  puVar13 = (undefined8 *)((long)&local_68 + 4U | 1);
  local_70 = (undefined1  [8])0x0;
  local_68 = 0;
  uVar16 = uVar15;
  do {
    lVar7 = (&DAT_ffff0000b80a5738)[uVar16];
    if ((*(byte *)(lVar7 + 0x34) & 3) != 0 || (uVar1 == 0 || param_4 == 0)) {
      return 0;
    }
    do {
      if (iVar17 == 0) {
        return 0;
      }
      if (((*(byte *)((&DAT_ffff0000b80a5738)[uVar15] + 0x34) & 3) == 0) &&
         (*(int *)(*(long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar15 * 8 + 0x10) + 0xc) < 0)) {
        iVar12 = 0;
        do {
          iVar12 = iVar12 + 1;
          lVar8 = lVar19;
          if (((uint)(iVar12 * 0x26e978d5) >> 3 | iVar12 * -0x60000000) < 0x418938) {
            do {
              lVar8 = lVar8 + -1;
            } while (lVar8 != 0);
          }
        } while (*(int *)(*(long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar15 * 8 + 0x10) + 0xc)
                 < 0);
      }
      if ((*(byte *)(lVar7 + 0xc) & 3) != 0) {
        lVar8 = *(long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar16 * 8 + 0x10);
        *(uint *)(lVar8 + 0xc) = *(uint *)(lVar8 + 0xc) & 0xfffffffe;
        *(uint *)(lVar8 + 0xc) = *(uint *)(lVar8 + 0xc) & 0xfffffffd;
      }
      puVar14 = *(uint **)(&DAT_ffff0000b80a2b00 + uVar16 * 8);
      *puVar14 = uVar3 << 0x10 | 0x3700;
      puVar14[1] = uVar6 | (uint)param_3 << 0x10;
      puVar18 = *(undefined4 **)(s_0123456789abcdef_ffff0000b80a2a80 + uVar16 * 8 + 0x10);
      puVar18[0x11] = 0x8002;
      puVar18[0x14] = puVar18[0x14] & 0xfffeffff;
      *puVar18 = 0;
      *puVar18 = 1;
      *puVar18 = 0;
      if ((((((((puVar18[3] & 1) == 0) && (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)) &&
             (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)) &&
            ((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
             (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)))) &&
           ((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
            ((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
             (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)))))) &&
          (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)) &&
         (((((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
             (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)) &&
            (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)) &&
           (((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
             (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)) &&
            ((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
             ((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
              (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)))))))) &&
          ((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
           ((FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0 &&
            (FUN_ffff0000b804b704(1000), (puVar18[3] & 1) == 0)))))))) {
        FUN_ffff0000b804b704(1000);
        lVar7 = lVar19;
        do {
          lVar7 = lVar7 + -1;
        } while (lVar7 != 0);
        puVar18[0x2d] = puVar18[0x2d] | 1;
        plVar10 = (long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar15 * 8 + 0x10);
        do {
          lVar7 = *plVar10;
          lVar19 = lVar19 + -1;
          *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) | 1;
          *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
          plVar10 = plVar10 + 1;
        } while (lVar19 != 0);
        return 0;
      }
      puVar18[0x2d] = puVar18[0x2d] | 1;
      puVar18[3] = puVar18[3] & 0xfffffffe;
      if (((((((uint)puVar18[3] >> 1 & 1) == 0) &&
            (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)) &&
           (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)) &&
          (((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
            (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)) &&
           ((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
            ((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
             (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)))))))) &&
         (((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
           ((((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
              (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)) &&
             (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)) &&
            ((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
             (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)))))) &&
          ((((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
             ((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
              (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)))) &&
            (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)) &&
           ((FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0 &&
            (FUN_ffff0000b804b704(1000), ((uint)puVar18[3] >> 1 & 1) == 0)))))))) {
        FUN_ffff0000b804b704(1000);
        lVar7 = lVar19;
        do {
          lVar7 = lVar7 + -1;
        } while (lVar7 != 0);
        puVar18[0x2d] = puVar18[0x2d] | 1;
        plVar10 = (long *)(s_0123456789abcdef_ffff0000b80a2a80 + uVar15 * 8 + 0x10);
        do {
          lVar7 = *plVar10;
          lVar19 = lVar19 + -1;
          *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) | 1;
          *(uint *)(lVar7 + 0x30) = *(uint *)(lVar7 + 0x30) & 0xfffffffe;
          plVar10 = plVar10 + 1;
        } while (lVar19 != 0);
        return 0;
      }
      puVar18[3] = puVar18[3] & 0xfffffffd;
      uVar4 = puVar18[0x29];
      uVar9 = (ulong)uVar4;
      local_68 = CONCAT44(uVar4,puVar18[0x2a]);
      local_70._4_4_ = puVar18[0x2b];
      local_70._0_4_ = puVar18[0x2c];
      if ((uVar4 & 0xff) < 0x23) {
        if ((1L << (uVar9 & 0x3f) & 0x14000000U) == 0) {
          puVar5 = puVar13;
          if ((1L << (uVar9 & 0x3f) & 0x200020000U) == 0) {
            if ((1L << (uVar9 & 0x3f) & 0x400040000U) == 0) goto LAB_ffff0000b8031208;
            uVar11 = 2;
            if (uVar1 < 2) goto LAB_ffff0000b8030dd4;
          }
          else {
            uVar11 = 1;
            if (uVar1 == 0) {
LAB_ffff0000b8030dd4:
              FUN_ffff0000b804cd40(param_4,puVar13,param_5);
              uVar11 = uVar1;
              goto LAB_ffff0000b8030dec;
            }
          }
        }
        else {
          uVar11 = (uVar4 >> 0xc & 0xff0) + (uVar4 >> 8 & 0xff);
          if (uVar1 <= uVar11) {
            uVar11 = uVar1;
          }
          if (9 < uVar11) {
            uVar11 = 10;
          }
          iVar12 = uVar11 - 4;
          puVar5 = &local_68;
          if (3 < uVar11 && iVar12 != 0) {
            FUN_ffff0000b804cd40(param_4,&local_68,4);
            puVar5 = (undefined8 *)((long)local_70 + 4);
            lVar8 = param_4 + 4;
            if (7 < uVar11 && uVar11 - 8 != 0) {
              FUN_ffff0000b804cd40(param_4 + 4,puVar5,4);
              puVar5 = (undefined8 *)local_70;
              lVar8 = param_4 + 8;
              iVar12 = uVar11 - 8;
            }
            FUN_ffff0000b804cd40(lVar8,puVar5,iVar12);
            goto LAB_ffff0000b8030dec;
          }
        }
        FUN_ffff0000b804cd40(param_4,puVar5,uVar11);
      }
      else {
LAB_ffff0000b8031208:
        uVar11 = 0;
        if ((uVar4 & 0xfd) != 0) {
          return 0;
        }
      }
LAB_ffff0000b8030dec:
      iVar17 = iVar17 + -1;
    } while ((uVar4 & 0xfd) == 0);
    uVar16 = uVar16 + 1;
    if (uVar16 == uVar2) {
      return uVar11;
    }
  } while( true );
}


