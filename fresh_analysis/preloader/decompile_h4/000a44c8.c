// File offset entry: 0xa44c8

undefined4 FUN_020a43c8(uint param_1)

{
  ulong uVar1;
  undefined4 uVar2;
  int iVar3;
  long lVar4;
  undefined8 uVar5;
  short sVar6;
  ulong uVar7;
  uint *puVar8;
  undefined4 uVar9;
  int iVar10;
  long lVar11;
  
  uRam0000000002122c18 = 0;
  uRam0000000002122c44 = 0;
  if (param_1 == 0) {
    uVar9 = 0x100000;
  }
  else {
    puVar8 = (uint *)(ulong)param_1;
    FUN_02091664(0x2122c18,0,0xb0);
    uVar9 = 0;
    if ((*puVar8 & 0xffffff) == 0x4d4d4d) {
      uVar9 = 0;
      iVar10 = 0;
      do {
        sVar6 = *(short *)((long)puVar8 + 6);
        if (sVar6 == 0) {
          uRam0000000002122c34 = (uint)(puVar8[0xd] >> 0x10 == 0xc260);
          if ((puVar8[0xd] >> 0xc & 1) != 0) {
            uRam0000000002122c44 = 1;
          }
          uRam0000000002122c3c = puVar8[0xb];
          uVar1 = (ulong)uRam0000000002122c3c;
          uRam0000000002122c1c = puVar8[0xc];
          uRam0000000002122c38 = (uint)*(byte *)((long)puVar8 + 0x1b);
          uRam0000000002122c20 = puVar8[8];
          uVar7 = (ulong)uRam0000000002122c20;
          if (uRam0000000002122c38 == 5) {
            if (uRam0000000002122c44 == 2) {
              lVar11 = uVar7 - uVar1;
              if (*(int *)((long)puVar8 + lVar11 + 4) == -0x1d6e0ca8) {
                uRam0000000002122c28 = (uint)*(ushort *)((long)puVar8 + lVar11 + 0x82e);
                iRam0000000002122c24 = 1;
                iVar3 = func_0x0209c58c((long)puVar8 + lVar11 + 0x14,0x40c,0x2122c48,2);
                uVar2 = 0x102004;
                if (iVar3 != 0) goto LAB_020a4674;
              }
              uVar2 = uVar9;
              if (*(int *)((long)puVar8 + lVar11 + 0xa30) == -0x1d6e0ca8) {
                lVar11 = lVar11 + 0xa30;
LAB_020a4624:
                lVar11 = (long)puVar8 + lVar11;
                FUN_02091664(0x2122c88,0,0x40);
                lVar4 = lVar11 + 0x1c;
                uVar5 = 0x30;
LAB_020a4648:
                FUN_020915ac(0x2122c88,lVar4,uVar5);
                uRam0000000002122c30 = (uint)*(ushort *)(lVar11 + 0x12);
                iRam0000000002122c2c = 1;
                uVar2 = uVar9;
              }
            }
            else if (uRam0000000002122c44 == 1) {
              lVar11 = uVar7 - uVar1;
              if (*(int *)((long)puVar8 + lVar11 + 4) == -0x1d6e0ca8) {
                uRam0000000002122c28 = (uint)*(ushort *)((long)puVar8 + lVar11 + 0x62e);
                iRam0000000002122c24 = 1;
                iVar3 = func_0x0209c58c((long)puVar8 + lVar11 + 0x14,0x30c,0x2122c48,1);
                uVar2 = 0x102004;
                if (iVar3 != 0) goto LAB_020a4674;
              }
              uVar2 = uVar9;
              if (*(int *)((long)puVar8 + lVar11 + 0x7b0) == -0x1d6e0ca8) {
                lVar11 = lVar11 + 0x7b0;
                goto LAB_020a4624;
              }
            }
            else {
              if (uRam0000000002122c44 != 0) {
                return 0x10101b;
              }
              lVar4 = uVar7 - uVar1;
              if (*(int *)((long)puVar8 + lVar4 + 4) == -0x1d6e0ca8) {
                uRam0000000002122c28 = (uint)*(ushort *)((long)puVar8 + lVar4 + 0x42e);
                iRam0000000002122c24 = 1;
                iVar3 = func_0x0209c58c((long)puVar8 + lVar4 + 0x14,0x20c,0x2122c48,0);
                uVar2 = 0x102004;
                if (iVar3 != 0) goto LAB_020a4674;
              }
              uVar2 = uVar9;
              if (*(int *)((long)puVar8 + lVar4 + 0x530) == -0x1d6e0ca8) {
                lVar11 = (long)puVar8 + lVar4 + 0x530;
                FUN_02091664(0x2122c88,0,0x40);
                lVar4 = (long)puVar8 + lVar4 + 0x54c;
                uVar5 = 0x20;
                goto LAB_020a4648;
              }
            }
LAB_020a4674:
            uVar9 = uVar2;
            if ((iRam0000000002122c24 == 1) && (iRam0000000002122c2c == 1)) {
              uRam0000000002122c18 = 1;
            }
          }
          sVar6 = *(short *)((long)puVar8 + 6);
        }
        if (sVar6 == 3) {
          iVar3 = func_0x0209c58c(puVar8 + 2,0x20c,0x2122c48,uRam0000000002122c44);
          if (iVar3 != 0) {
            return 0x102004;
          }
          sVar6 = *(short *)((long)puVar8 + 6);
          uRam0000000002122c18 = 1;
        }
        if (sVar6 == 8) {
          if (puVar8[0xb] == 0xc975e033) {
            uRam0000000002122c40 = 1;
            sVar6 = *(short *)((long)puVar8 + 6);
            goto LAB_020a46f8;
          }
        }
        else {
LAB_020a46f8:
          if (sVar6 == 0x60) {
            uRam0000000002122c44 = puVar8[2];
          }
        }
        iVar10 = iVar10 + (uint)(ushort)puVar8[1];
        puVar8 = (uint *)(ulong)(iVar10 + param_1);
      } while ((*puVar8 & 0xffffff) == 0x4d4d4d);
    }
  }
  return uVar9;
}


