// ffff000090745300  size=2144  FUN_ffff000090745300

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

void FUN_ffff000090745300(int param_1,int param_2,byte param_3)

{
  uint uVar1;
  byte bVar2;
  long lVar3;
  long lVar4;
  uint uVar5;
  uint uVar6;
  bool bVar7;
  int iVar8;
  int iVar9;
  long lVar10;
  byte *pbVar11;
  uint *puVar12;
  ulong uVar13;
  long lVar14;
  ulong uVar15;
  int iVar16;
  ulong *puVar17;
  
  uVar6 = _DAT_ffff0000908775a4;
  uVar5 = _DAT_ffff0000908775a0;
  lVar4 = _DAT_ffff000090877590;
  puVar17 = _DAT_ffff0000908775b8;
  if (_DAT_ffff000090877590 == 0) {
code_r0xffff000090745370:
    iVar9 = -0x10;
    pbVar11 = &DAT_ffff000090804fb0 + (ulong)param_3 * 0x10;
    iVar16 = 0xf;
    iVar8 = iVar9;
    do {
      while( true ) {
        uVar1 = *(uint *)(&DAT_ffff000090804db0 + ((ulong)*pbVar11 & 0xf) * 4);
        *puVar17 = (ulong)(*(uint *)(&DAT_ffff000090804db0 + (ulong)(*pbVar11 >> 4) * 4) & uVar6 ^
                          uVar5);
        puVar17[1] = (ulong)(uVar1 & uVar6 ^ uVar5);
        if (lVar4 != 0) break;
        bVar7 = iVar9 == -1;
        iVar9 = iVar9 + 1;
        pbVar11 = pbVar11 + 1;
        if (bVar7) {
          return;
        }
      }
      iVar9 = iVar8 + 1;
      puVar17 = (ulong *)((long)puVar17 +
                         (ulong)(uint)(*(int *)(lVar4 + 0x14) * *(int *)(lVar4 + 0x10)));
      bVar7 = iVar16 != 0;
      pbVar11 = pbVar11 + 1;
      iVar16 = iVar16 + -1;
      iVar8 = iVar9;
    } while (bVar7);
  }
  else {
    if (5 < *(uint *)(_DAT_ffff000090877590 + 0xc)) {
      return;
    }
    iVar9 = *(int *)(_DAT_ffff000090877590 + 0x10);
    iVar16 = *(int *)(_DAT_ffff000090877590 + 0x14) * iVar9;
    puVar17 = (ulong *)((long)_DAT_ffff0000908775b8 +
                       (ulong)(uint)(iVar16 * param_2) + (ulong)(uint)(iVar9 * param_1));
    switch(*(uint *)(_DAT_ffff000090877590 + 0xc)) {
    default:
      goto code_r0xffff000090745370;
    case 1:
      lVar10 = 0;
      do {
        bVar2 = (&DAT_ffff000090804fb0)[lVar10 + (ulong)param_3 * 0x10];
        lVar10 = lVar10 + 1;
        *(uint *)puVar17 =
             *(uint *)(&DAT_ffff000090804c10 + ((ulong)(bVar2 >> 4) & 0xc)) & uVar6 ^ uVar5;
        *(uint *)((long)puVar17 + 4) =
             *(uint *)(&DAT_ffff000090804c10 + ((ulong)(bVar2 >> 4) & 3) * 4) & uVar6 ^ uVar5;
        *(uint *)(puVar17 + 1) =
             *(uint *)(&DAT_ffff000090804c10 + ((ulong)bVar2 & 0xc)) & uVar6 ^ uVar5;
        *(uint *)((long)puVar17 + 0xc) =
             *(uint *)(&DAT_ffff000090804c10 + ((ulong)bVar2 & 3) * 4) & uVar6 ^ uVar5;
        puVar17 = (ulong *)((long)puVar17 +
                           (ulong)(uint)(*(int *)(lVar4 + 0x10) * *(int *)(lVar4 + 0x14)));
      } while ((int)lVar10 != 0x10);
      break;
    case 2:
      iVar9 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f6757,3);
      lVar10 = 0;
      if (iVar9 == 0) {
        puVar12 = (uint *)((long)puVar17 + (ulong)(uint)(iVar16 * 0x10));
        do {
          bVar2 = (&DAT_ffff000090804fb0)[lVar10 + (ulong)param_3 * 0x10];
          lVar10 = lVar10 + 1;
          uVar1 = *(uint *)(&DAT_ffff000090804d50 + ((ulong)bVar2 & 3) * 4) & uVar6 ^ uVar5;
          *puVar12 = uVar1 >> 0x10 | uVar1 << 0x10;
          uVar1 = *(uint *)(&DAT_ffff000090804d50 + ((ulong)bVar2 & 0xc)) & uVar6 ^ uVar5;
          puVar12[1] = uVar1 >> 0x10 | uVar1 << 0x10;
          uVar1 = *(uint *)(&DAT_ffff000090804d50 + ((ulong)(bVar2 >> 4) & 3) * 4) & uVar6 ^ uVar5;
          puVar12[2] = uVar1 >> 0x10 | uVar1 << 0x10;
          uVar1 = *(uint *)(&DAT_ffff000090804d50 + ((ulong)(bVar2 >> 4) & 0xc)) & uVar6 ^ uVar5;
          puVar12[3] = uVar1 >> 0x10 | uVar1 << 0x10;
          puVar12 = (uint *)((long)puVar12 -
                            (ulong)(uint)(*(int *)(lVar4 + 0x10) * *(int *)(lVar4 + 0x14)));
        } while ((int)lVar10 != 0x10);
      }
      else {
        do {
          bVar2 = (&DAT_ffff000090804fb0)[lVar10 + (ulong)param_3 * 0x10];
          lVar10 = lVar10 + 1;
          *(uint *)puVar17 =
               *(uint *)(&DAT_ffff000090804d50 + ((ulong)(bVar2 >> 4) & 0xc)) & uVar6 ^ uVar5;
          *(uint *)((long)puVar17 + 4) =
               *(uint *)(&DAT_ffff000090804d50 + ((ulong)(bVar2 >> 4) & 3) * 4) & uVar6 ^ uVar5;
          *(uint *)(puVar17 + 1) =
               *(uint *)(&DAT_ffff000090804d50 + ((ulong)bVar2 & 0xc)) & uVar6 ^ uVar5;
          *(uint *)((long)puVar17 + 0xc) =
               *(uint *)(&DAT_ffff000090804d50 + ((ulong)bVar2 & 3) * 4) & uVar6 ^ uVar5;
          puVar17 = (ulong *)((long)puVar17 +
                             (ulong)(uint)(*(int *)(lVar4 + 0x10) * *(int *)(lVar4 + 0x14)));
        } while ((int)lVar10 != 0x10);
      }
      break;
    case 3:
      iVar8 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907d9514,2);
      if (iVar8 == 0) {
        lVar10 = 0;
        lVar14 = (long)puVar17 + (ulong)(uint)(iVar9 << 4);
        do {
          uVar13 = (ulong)(byte)(&DAT_ffff000090804fb0)[lVar10 + (ulong)param_3 * 0x10];
          lVar10 = lVar10 + 1;
          uVar15 = uVar13 & 0xf0;
          lVar3 = (uVar13 & 0xf) * 0x10;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) << 4) * 4) =
               *(uint *)(&DAT_ffff000090804eb0 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x11) * 4) =
               *(uint *)(&DAT_ffff000090804eb4 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x12) * 4) =
               *(uint *)(&DAT_ffff000090804eb8 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x13) * 4) =
               *(uint *)(&DAT_ffff000090804ebc + uVar15) & uVar6 ^ uVar5 | 0xff000000;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x14) * 4) =
               *(uint *)(&DAT_ffff000090804eb0 + lVar3) & uVar6 ^ uVar5 | 0xff000000;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x15) * 4) =
               *(uint *)(&DAT_ffff000090804eb4 + lVar3) & uVar6 ^ uVar5 | 0xff000000;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x16) * 4) =
               *(uint *)(&DAT_ffff000090804eb8 + lVar3) & uVar6 ^ uVar5 | 0xff000000;
          *(uint *)(lVar14 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x17) * 4) =
               *(uint *)(&DAT_ffff000090804ebc + lVar3) & uVar6 ^ uVar5 | 0xff000000;
          lVar14 = lVar14 - (ulong)*(uint *)(lVar4 + 0x10);
        } while ((int)lVar10 != 0x10);
      }
      else {
        iVar9 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f6757,3);
        if (iVar9 == 0) {
          lVar10 = 0;
          puVar12 = (uint *)((long)puVar17 + (ulong)(uint)(iVar16 * 0x10));
          do {
            uVar13 = (ulong)(byte)(&DAT_ffff000090804fb0)[lVar10 + (ulong)param_3 * 0x10];
            lVar10 = lVar10 + 1;
            uVar15 = uVar13 & 0xf0;
            lVar14 = (uVar13 & 0xf) * 0x10;
            puVar12[7] = *(uint *)(&DAT_ffff000090804eb0 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
            puVar12[6] = *(uint *)(&DAT_ffff000090804eb4 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
            puVar12[5] = *(uint *)(&DAT_ffff000090804eb8 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
            puVar12[4] = *(uint *)(&DAT_ffff000090804ebc + uVar15) & uVar6 ^ uVar5 | 0xff000000;
            puVar12[3] = *(uint *)(&DAT_ffff000090804eb0 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
            puVar12[2] = *(uint *)(&DAT_ffff000090804eb4 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
            puVar12[1] = *(uint *)(&DAT_ffff000090804eb8 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
            *puVar12 = *(uint *)(&DAT_ffff000090804ebc + lVar14) & uVar6 ^ uVar5 | 0xff000000;
            puVar12 = (uint *)((long)puVar12 -
                              (ulong)(uint)(*(int *)(lVar4 + 0x10) * *(int *)(lVar4 + 0x14)));
          } while ((int)lVar10 != 0x10);
        }
        else {
          iVar9 = FUN_ffff00009077f348(&DAT_ffff0000907df338,&DAT_ffff0000907f2fb1,3);
          lVar10 = 0;
          if (iVar9 == 0) {
            do {
              uVar13 = (ulong)(byte)(&DAT_ffff000090804fb0)[lVar10 + (ulong)param_3 * 0x10];
              lVar10 = lVar10 + 1;
              uVar15 = uVar13 & 0xf0;
              lVar14 = (uVar13 & 0xf) * 0x10;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x17) * 4) =
                   *(uint *)(&DAT_ffff000090804eb0 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x16) * 4) =
                   *(uint *)(&DAT_ffff000090804eb4 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x15) * 4) =
                   *(uint *)(&DAT_ffff000090804eb8 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x14) * 4) =
                   *(uint *)(&DAT_ffff000090804ebc + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x13) * 4) =
                   *(uint *)(&DAT_ffff000090804eb0 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x12) * 4) =
                   *(uint *)(&DAT_ffff000090804eb4 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) * 0x11) * 4) =
                   *(uint *)(&DAT_ffff000090804eb8 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + (ulong)(uint)(*(int *)(lVar4 + 0x14) << 4) * 4) =
                   *(uint *)(&DAT_ffff000090804ebc + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              puVar17 = (ulong *)((long)puVar17 + (ulong)*(uint *)(lVar4 + 0x10));
            } while ((int)lVar10 != 0x10);
          }
          else {
            do {
              uVar13 = (ulong)(byte)(&DAT_ffff000090804fb0)[lVar10 + (ulong)param_3 * 0x10];
              lVar10 = lVar10 + 1;
              uVar15 = uVar13 & 0xf0;
              lVar14 = (uVar13 & 0xf) * 0x10;
              *(uint *)puVar17 =
                   *(uint *)(&DAT_ffff000090804eb0 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + 4) =
                   *(uint *)(&DAT_ffff000090804eb4 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)(puVar17 + 1) =
                   *(uint *)(&DAT_ffff000090804eb8 + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + 0xc) =
                   *(uint *)(&DAT_ffff000090804ebc + uVar15) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)(puVar17 + 2) =
                   *(uint *)(&DAT_ffff000090804eb0 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + 0x14) =
                   *(uint *)(&DAT_ffff000090804eb4 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)(puVar17 + 3) =
                   *(uint *)(&DAT_ffff000090804eb8 + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              *(uint *)((long)puVar17 + 0x1c) =
                   *(uint *)(&DAT_ffff000090804ebc + lVar14) & uVar6 ^ uVar5 | 0xff000000;
              puVar17 = (ulong *)((long)puVar17 +
                                 (ulong)(uint)(*(int *)(lVar4 + 0x10) * *(int *)(lVar4 + 0x14)));
            } while ((int)lVar10 != 0x10);
          }
        }
      }
      break;
    case 4:
      lVar10 = 0;
      do {
        lVar3 = lVar10 + (ulong)param_3 * 0x10;
        lVar10 = lVar10 + 1;
        lVar14 = (ulong)((byte)(&DAT_ffff000090804fb0)[lVar3] >> 4) * 0xc;
        lVar3 = ((ulong)(byte)(&DAT_ffff000090804fb0)[lVar3] & 0xf) * 0xc;
        *(uint *)puVar17 = *(uint *)(&DAT_ffff000090804df0 + lVar14) & uVar6 ^ uVar5;
        *(uint *)((long)puVar17 + 4) = *(uint *)(&DAT_ffff000090804df4 + lVar14) & uVar6 ^ uVar5;
        *(uint *)(puVar17 + 1) = *(uint *)(&DAT_ffff000090804df8 + lVar14) & uVar6 ^ uVar5;
        *(uint *)((long)puVar17 + 0xc) = *(uint *)(&DAT_ffff000090804df0 + lVar3) & uVar6 ^ uVar5;
        *(uint *)(puVar17 + 2) = *(uint *)(&DAT_ffff000090804df4 + lVar3) & uVar6 ^ uVar5;
        *(uint *)((long)puVar17 + 0x14) = *(uint *)(&DAT_ffff000090804df8 + lVar3) & uVar6 ^ uVar5;
        puVar17 = (ulong *)((long)puVar17 +
                           (ulong)(uint)(*(int *)(lVar4 + 0x10) * *(int *)(lVar4 + 0x14)));
      } while ((int)lVar10 != 0x10);
    }
  }
  return;
}


