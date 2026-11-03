// ffff00009074f5b0  size=1832  FUN_ffff00009074f5b0

uint FUN_ffff00009074f5b0(int param_1,undefined8 param_2,long param_3,byte *param_4,uint param_5)

{
  ulong uVar1;
  byte bVar2;
  undefined8 *puVar3;
  uint uVar4;
  long lVar5;
  long *plVar6;
  uint uVar7;
  int iVar8;
  uint uVar9;
  undefined8 *puVar10;
  ulong uVar11;
  uint *puVar12;
  uint uVar13;
  int iVar14;
  undefined4 *puVar15;
  long lVar16;
  ulong uVar17;
  ulong uVar18;
  long local_78;
  undefined1 local_70 [8];
  undefined8 local_68;
  
  bVar2 = *param_4;
  param_5 = param_5 & 0xff;
  uVar7 = 0;
  if (param_1 != 0x2b) {
    uVar7 = (uint)(param_1 != 0x2d);
  }
  uVar17 = (ulong)uVar7;
  uVar1 = 1;
  if (param_1 != 0x2b) {
    uVar1 = 2;
  }
  uVar7 = 0x604;
  if (0xaf < bVar2) {
    uVar7 = 0x1404;
  }
  iVar14 = 5;
  uVar13 = param_5;
  if (9 < param_5) {
    uVar13 = 10;
  }
  puVar10 = (undefined8 *)((long)&local_68 + 4U | 1);
  local_70 = (undefined1  [8])0x0;
  local_68 = 0;
  local_78 = uVar1 - uVar17;
  uVar18 = uVar17;
  do {
    lVar16 = (&DAT_ffff00009080cf90)[uVar18];
    if ((*(byte *)(lVar16 + 0x34) & 3) != 0 || (param_5 == 0 || param_3 == 0)) {
      return 0;
    }
    do {
      if (iVar14 == 0) {
        return 0;
      }
      if (((*(byte *)((&DAT_ffff00009080cf90)[uVar17] + 0x34) & 3) == 0) &&
         (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar17 * 8 + 0x10) + 0xc) < 0)) {
        iVar8 = 0;
        do {
          iVar8 = iVar8 + 1;
          lVar5 = local_78;
          if (((uint)(iVar8 * 0x26e978d5) >> 3 | iVar8 * -0x60000000) < 0x418938) {
            do {
              lVar5 = lVar5 + -1;
            } while (lVar5 != 0);
          }
        } while (*(int *)(*(long *)(s_0123456789abcdef_ffff000090804c70 + uVar17 * 8 + 0x10) + 0xc)
                 < 0);
      }
      if ((*(byte *)(lVar16 + 0xc) & 3) != 0) {
        lVar5 = *(long *)(s_0123456789abcdef_ffff000090804c70 + uVar18 * 8 + 0x10);
        *(uint *)(lVar5 + 0xc) = *(uint *)(lVar5 + 0xc) & 0xfffffffe;
        *(uint *)(lVar5 + 0xc) = *(uint *)(lVar5 + 0xc) & 0xfffffffd;
      }
      puVar12 = *(uint **)(&DAT_ffff000090804cf0 + uVar18 * 8);
      *puVar12 = uVar13 << 0x10 | 0x3700;
      puVar12[1] = uVar7 | (uint)bVar2 << 0x10;
      puVar15 = *(undefined4 **)(s_0123456789abcdef_ffff000090804c70 + uVar18 * 8 + 0x10);
      puVar15[0x11] = 0x8002;
      puVar15[0x14] = puVar15[0x14] & 0xfffeffff;
      *puVar15 = 0;
      *puVar15 = 1;
      *puVar15 = 0;
      if ((((((((puVar15[3] & 1) == 0) && (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)) &&
             (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)) &&
            ((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
             (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)))) &&
           ((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
            ((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
             (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)))))) &&
          (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)) &&
         (((((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
             (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)) &&
            (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)) &&
           (((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
             (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)) &&
            ((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
             ((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
              (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)))))))) &&
          ((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
           ((FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0 &&
            (FUN_ffff00009077cea0(1000), (puVar15[3] & 1) == 0)))))))) {
        FUN_ffff00009077cea0(1000);
        lVar16 = local_78;
        do {
          lVar16 = lVar16 + -1;
        } while (lVar16 != 0);
        puVar15[0x2d] = puVar15[0x2d] | 1;
        plVar6 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar17 * 8 + 0x10);
        do {
          lVar16 = *plVar6;
          *(uint *)(lVar16 + 0x30) = *(uint *)(lVar16 + 0x30) | 1;
          local_78 = local_78 + -1;
          *(uint *)(lVar16 + 0x30) = *(uint *)(lVar16 + 0x30) & 0xfffffffe;
          plVar6 = plVar6 + 1;
        } while (local_78 != 0);
        return 0;
      }
      puVar15[0x2d] = puVar15[0x2d] | 1;
      puVar15[3] = puVar15[3] & 0xfffffffe;
      if (((((((uint)puVar15[3] >> 1 & 1) == 0) &&
            (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)) &&
           (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)) &&
          (((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
            (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)) &&
           ((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
            ((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
             (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)))))))) &&
         (((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
           ((((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
              (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)) &&
             (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)) &&
            ((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
             (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)))))) &&
          ((((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
             ((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
              (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)))) &&
            (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)) &&
           ((FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0 &&
            (FUN_ffff00009077cea0(1000), ((uint)puVar15[3] >> 1 & 1) == 0)))))))) {
        FUN_ffff00009077cea0(1000);
        lVar16 = local_78;
        do {
          lVar16 = lVar16 + -1;
        } while (lVar16 != 0);
        puVar15[0x2d] = puVar15[0x2d] | 1;
        plVar6 = (long *)(s_0123456789abcdef_ffff000090804c70 + uVar17 * 8 + 0x10);
        do {
          lVar16 = *plVar6;
          *(uint *)(lVar16 + 0x30) = *(uint *)(lVar16 + 0x30) | 1;
          local_78 = local_78 + -1;
          *(uint *)(lVar16 + 0x30) = *(uint *)(lVar16 + 0x30) & 0xfffffffe;
          plVar6 = plVar6 + 1;
        } while (local_78 != 0);
        return 0;
      }
      iVar14 = iVar14 + -1;
      puVar15[3] = puVar15[3] & 0xfffffffd;
      uVar4 = puVar15[0x29];
      local_68 = CONCAT44(uVar4,puVar15[0x2a]);
      uVar9 = uVar4 & 0xff;
      uVar11 = (ulong)uVar9;
      local_70._4_4_ = puVar15[0x2b];
      local_70._0_4_ = puVar15[0x2c];
    } while (uVar9 == 2);
    if (0x22 < uVar9) {
      return 0;
    }
    lVar16 = param_3;
    if ((1L << (uVar11 & 0x3f) & 0x14000000U) == 0) {
      if ((1L << (uVar11 & 0x3f) & 0x200020000U) == 0) {
        if ((1L << (uVar11 & 0x3f) & 0x400040000U) == 0) {
          return 0;
        }
        uVar9 = 2;
      }
      else {
        uVar9 = 1;
      }
      puVar3 = puVar10;
      uVar4 = uVar9;
      if (uVar9 <= param_5) goto LAB_ffff00009074fb78;
      FUN_ffff00009077eb20(param_3,puVar10);
      uVar9 = param_5;
    }
    else {
      uVar9 = (uVar4 >> 0xc & 0xff0) + (uVar4 >> 8 & 0xff);
      if (param_5 <= uVar9) {
        uVar9 = param_5;
      }
      if (9 < uVar9) {
        uVar9 = 10;
      }
      puVar3 = &local_68;
      uVar4 = uVar9;
      if (3 < uVar9 && uVar9 - 4 != 0) {
        FUN_ffff00009077eb20(param_3,&local_68,4);
        lVar16 = param_3 + 4;
        puVar3 = (undefined8 *)(local_70 + 4);
        uVar4 = uVar9 - 4;
        if (7 < uVar9 && uVar9 - 8 != 0) {
          FUN_ffff00009077eb20(param_3 + 4,local_70 + 4,4);
          lVar16 = param_3 + 8;
          puVar3 = (undefined8 *)local_70;
          uVar4 = uVar9 - 8;
        }
      }
LAB_ffff00009074fb78:
      FUN_ffff00009077eb20(lVar16,puVar3,uVar4);
    }
    uVar18 = uVar18 + 1;
    if (uVar18 == uVar1) {
      return uVar9;
    }
  } while( true );
}


