
ulong FUN_ffff0000be054bf8(uint param_1,ulong *param_2,ulong param_3)

{
  byte *pbVar1;
  ulong uVar2;
  ulong uVar3;
  ulong uVar4;
  ulong uVar5;
  uint uVar6;
  ulong uVar7;
  ulong uVar8;
  ulong uVar9;
  ulong uVar10;
  ulong uVar11;
  ulong uVar12;
  ulong *puVar13;
  long lVar14;
  ulong uVar15;
  ulong *puVar16;
  
  if (param_2 == (ulong *)0x0) {
    return 0;
  }
  param_1 = ~param_1;
  uVar10 = (ulong)param_1;
  if (0x2e < param_3) {
    puVar13 = param_2;
    uVar8 = param_3;
    if (((ulong)param_2 & 7) != 0) {
      puVar13 = (ulong *)((long)param_2 + 1);
      uVar8 = param_3 - 1;
      uVar6 = *(uint *)(&DAT_ffff0000be0cf128 + ((ulong)((byte)*param_2 ^ param_1) & 0xff) * 4) ^
              param_1 >> 8;
      uVar10 = (ulong)uVar6;
      if ((uVar8 != 0) && (((ulong)puVar13 & 7) != 0)) {
        puVar13 = (ulong *)((long)param_2 + 2);
        uVar8 = param_3 - 2;
        uVar6 = *(uint *)(&DAT_ffff0000be0cf128 +
                         ((ulong)(*(byte *)((long)param_2 + 1) ^ uVar6) & 0xff) * 4) ^ uVar6 >> 8;
        uVar10 = (ulong)uVar6;
        if ((uVar8 != 0) && (((ulong)puVar13 & 7) != 0)) {
          puVar13 = (ulong *)((long)param_2 + 3);
          uVar8 = param_3 - 3;
          uVar6 = *(uint *)(&DAT_ffff0000be0cf128 +
                           ((ulong)(*(byte *)((long)param_2 + 2) ^ uVar6) & 0xff) * 4) ^ uVar6 >> 8;
          uVar10 = (ulong)uVar6;
          if ((uVar8 != 0) && (((ulong)puVar13 & 7) != 0)) {
            puVar13 = (ulong *)((long)param_2 + 4);
            uVar8 = param_3 - 4;
            uVar6 = *(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)(*(byte *)((long)param_2 + 3) ^ uVar6) & 0xff) * 4) ^
                    uVar6 >> 8;
            uVar10 = (ulong)uVar6;
            if ((uVar8 != 0) && (((ulong)puVar13 & 7) != 0)) {
              puVar13 = (ulong *)((long)param_2 + 5);
              uVar8 = param_3 - 5;
              uVar6 = *(uint *)(&DAT_ffff0000be0cf128 +
                               ((ulong)(*(byte *)((long)param_2 + 4) ^ uVar6) & 0xff) * 4) ^
                      uVar6 >> 8;
              uVar10 = (ulong)uVar6;
              if ((uVar8 != 0) && (((ulong)puVar13 & 7) != 0)) {
                puVar13 = (ulong *)((long)param_2 + 6);
                uVar8 = param_3 - 6;
                uVar6 = *(uint *)(&DAT_ffff0000be0cf128 +
                                 ((ulong)(*(byte *)((long)param_2 + 5) ^ uVar6) & 0xff) * 4) ^
                        uVar6 >> 8;
                uVar10 = (ulong)uVar6;
                if ((uVar8 != 0) && (((ulong)puVar13 & 7) != 0)) {
                  puVar13 = (ulong *)((long)param_2 + 7);
                  uVar8 = param_3 - 7;
                  uVar6 = *(uint *)(&DAT_ffff0000be0cf128 +
                                   ((ulong)(*(byte *)((long)param_2 + 6) ^ uVar6) & 0xff) * 4) ^
                          uVar6 >> 8;
                  uVar10 = (ulong)uVar6;
                  if ((uVar8 != 0) && (((ulong)puVar13 & 7) != 0)) {
                    puVar13 = param_2 + 1;
                    uVar8 = param_3 - 8;
                    uVar10 = (ulong)(*(uint *)(&DAT_ffff0000be0cf128 +
                                              ((ulong)(*(byte *)((long)param_2 + 7) ^ uVar6) & 0xff)
                                              * 4) ^ uVar6 >> 8);
                  }
                }
              }
            }
          }
        }
      }
    }
    lVar14 = uVar8 / 0x28 - 1;
    param_3 = uVar8 % 0x28;
    if (lVar14 == 0) {
      uVar9 = 0;
      uVar7 = 0;
      uVar15 = 0;
      uVar12 = 0;
    }
    else {
      uVar12 = 0;
      uVar15 = 0;
      uVar7 = 0;
      uVar9 = 0;
      puVar16 = puVar13;
      do {
        uVar2 = *puVar16;
        uVar4 = puVar16[1];
        uVar3 = puVar16[2];
        uVar5 = puVar16[3];
        uVar10 = uVar2 ^ uVar10;
        uVar11 = puVar16[4];
        uVar9 = uVar4 ^ uVar9;
        uVar7 = uVar3 ^ uVar7;
        uVar15 = uVar5 ^ uVar15;
        uVar12 = uVar11 ^ uVar12;
        lVar14 = lVar14 + -1;
        puVar16 = puVar16 + 5;
        uVar10 = (ulong)(*(uint *)(&DAT_ffff0000be0d1128 + (uVar2 >> 0x36 & 0x3fc)) ^
                        *(uint *)(&DAT_ffff0000be0d0d28 + (uVar2 >> 0x30 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0928 + (uVar2 >> 0x28 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0528 + (uVar2 >> 0x20 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0128 + (uVar10 >> 0x18 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cfd28 + (uVar10 >> 0x10 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cf928 + (uVar10 >> 8 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cf528 + (uVar10 & 0xff) * 4));
        uVar9 = (ulong)(*(uint *)(&DAT_ffff0000be0d1128 + (uVar4 >> 0x36 & 0x3fc)) ^
                       *(uint *)(&DAT_ffff0000be0d0d28 + (uVar4 >> 0x30 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0d0928 + (uVar4 >> 0x28 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0d0528 + (uVar4 >> 0x20 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0d0128 + (uVar9 >> 0x18 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0cfd28 + (uVar9 >> 0x10 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0cf928 + (uVar9 >> 8 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0cf528 + (uVar9 & 0xff) * 4));
        uVar7 = (ulong)(*(uint *)(&DAT_ffff0000be0d1128 + (uVar3 >> 0x36 & 0x3fc)) ^
                       *(uint *)(&DAT_ffff0000be0d0d28 + (uVar3 >> 0x30 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0d0928 + (uVar3 >> 0x28 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0d0528 + (uVar3 >> 0x20 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0d0128 + (uVar7 >> 0x18 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0cfd28 + (uVar7 >> 0x10 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0cf928 + (uVar7 >> 8 & 0xff) * 4) ^
                       *(uint *)(&DAT_ffff0000be0cf528 + (uVar7 & 0xff) * 4));
        uVar15 = (ulong)(*(uint *)(&DAT_ffff0000be0d1128 + (uVar5 >> 0x36 & 0x3fc)) ^
                        *(uint *)(&DAT_ffff0000be0d0d28 + (uVar5 >> 0x30 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0928 + (uVar5 >> 0x28 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0528 + (uVar5 >> 0x20 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0128 + (uVar15 >> 0x18 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cfd28 + (uVar15 >> 0x10 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cf928 + (uVar15 >> 8 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cf528 + (uVar15 & 0xff) * 4));
        uVar12 = (ulong)(*(uint *)(&DAT_ffff0000be0d1128 + (uVar11 >> 0x36 & 0x3fc)) ^
                        *(uint *)(&DAT_ffff0000be0d0d28 + (uVar11 >> 0x30 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0928 + (uVar11 >> 0x28 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0528 + (uVar11 >> 0x20 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0d0128 + (uVar12 >> 0x18 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cfd28 + (uVar12 >> 0x10 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cf928 + (uVar12 >> 8 & 0xff) * 4) ^
                        *(uint *)(&DAT_ffff0000be0cf528 + (uVar12 & 0xff) * 4));
      } while (lVar14 != 0);
      puVar13 = puVar13 + (uVar8 / 0x28) * 5 + -5;
    }
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + ((*puVar13 ^ uVar10) & 0xff) * 4) ^
             (*puVar13 ^ uVar10) >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = puVar13[1] ^ uVar9 ^
             (ulong)(*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ (uint)(uVar10 >> 8));
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = puVar13[2] ^ uVar7 ^
             (ulong)(*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ (uint)(uVar10 >> 8));
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = puVar13[3] ^ uVar15 ^
             (ulong)(*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ (uint)(uVar10 >> 8));
    param_2 = puVar13 + 5;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = puVar13[4] ^ uVar12 ^
             (ulong)(*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ (uint)(uVar10 >> 8));
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ uVar10 >> 8;
    uVar10 = (ulong)(*(uint *)(&DAT_ffff0000be0cf128 + (uVar10 & 0xff) * 4) ^ (uint)(uVar10 >> 8));
  }
  for (; 7 < param_3; param_3 = param_3 - 8) {
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)(byte)*param_2 ^ (uint)uVar10) & 0xff) * 4) ^
             uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)*(byte *)((long)param_2 + 1) ^ (uint)uVar10) & 0xff) * 4
                             ) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)*(byte *)((long)param_2 + 2) ^ (uint)uVar10) & 0xff) * 4
                             ) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)*(byte *)((long)param_2 + 3) ^ (uint)uVar10) & 0xff) * 4
                             ) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)*(byte *)((long)param_2 + 4) ^ (uint)uVar10) & 0xff) * 4
                             ) ^ uVar10 >> 8;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)*(byte *)((long)param_2 + 5) ^ (uint)uVar10) & 0xff) * 4
                             ) ^ uVar10 >> 8;
    pbVar1 = (byte *)((long)param_2 + 7);
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)*(byte *)((long)param_2 + 6) ^ (uint)uVar10) & 0xff) * 4
                             ) ^ uVar10 >> 8;
    param_2 = param_2 + 1;
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)*pbVar1 ^ (uint)uVar10) & 0xff) * 4) ^ uVar10 >> 8;
  }
  for (; param_3 != 0; param_3 = param_3 - 1) {
    uVar10 = (ulong)*(uint *)(&DAT_ffff0000be0cf128 +
                             ((ulong)((uint)(byte)*param_2 ^ (uint)uVar10) & 0xff) * 4) ^
             uVar10 >> 8;
    param_2 = (ulong *)((long)param_2 + 1);
  }
  return uVar10 ^ 0xffffffff;
}


