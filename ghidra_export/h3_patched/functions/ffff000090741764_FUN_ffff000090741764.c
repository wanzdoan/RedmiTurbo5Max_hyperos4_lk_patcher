// ffff000090741764  size=480  FUN_ffff000090741764

void FUN_ffff000090741764(void)

{
  long lVar1;
  int iVar2;
  bool bVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  bool bVar7;
  int iVar8;
  byte *pbVar9;
  long lVar10;
  long lVar11;
  undefined8 uVar12;
  
  lVar11 = 0;
  uVar12 = 0x20000;
  bVar7 = true;
  do {
    lVar10 = lVar11 * 0x4000;
    lVar1 = lVar10 + -0xffff6f790c20;
    *(undefined4 *)(&DAT_ffff00009086f3d8 + lVar11 * 4) = 0;
    FUN_ffff00009077eb88(lVar1,0,0x4000);
    *(long *)(&DAT_ffff0000908773e8 + lVar11 * 0x20) = lVar10 + -0xffff6f790c18;
    lVar5 = FUN_ffff00009077bd60(&DAT_ffff0000907d93eb);
    if (lVar5 != 0) {
      lVar6 = FUN_ffff00009077bdf4(lVar5,lVar1,uVar12,0x4000);
      FUN_ffff00009077bce4(lVar5);
      if (lVar6 == 0x4000) {
        lVar5 = lVar11 * 0x20;
        FUN_ffff00009077e858(lVar5 + -0xffff6f788c20,lVar1,8);
        FUN_ffff00009077e858(lVar5 + -0xffff6f788c10,lVar10 + -0xffff6f78cc2c,8);
        *(undefined1 *)(lVar5 + -0xffff6f788c19) = 0;
        *(undefined1 *)(lVar5 + -0xffff6f788c09) = 0;
        iVar4 = FUN_ffff00009077ede4(lVar5 + -0xffff6f788c20,s_ENV_v1_ffff0000907d4165);
        if ((iVar4 == 0) &&
           (iVar4 = FUN_ffff00009077ede4(lVar5 + -0xffff6f788c10,s_ENV_v1_ffff0000907d4165),
           iVar4 == 0)) {
          lVar5 = *(long *)(&DAT_ffff0000908773e8 + lVar11 * 0x20);
          iVar4 = 0;
          iVar8 = 0;
          iVar2 = *(int *)(&DAT_ffff0000908733dc + lVar10);
          lVar10 = 0x3fec;
          *(int *)(lVar11 * 0x20 + -0xffff6f788c08) = iVar2;
          pbVar9 = (byte *)(lVar5 + 1);
          do {
            lVar10 = lVar10 + -2;
            iVar4 = iVar4 + (uint)pbVar9[-1];
            iVar8 = iVar8 + (uint)*pbVar9;
            pbVar9 = pbVar9 + 2;
          } while (lVar10 != 0);
          if (iVar8 + iVar4 == iVar2) {
            FUN_ffff00009077de54(s_ENV_of_area__d_initialize_succes_ffff0000907e2f4c,lVar11);
            *(undefined4 *)(&DAT_ffff00009086f3d8 + lVar11 * 4) = 1;
            goto LAB_ffff0000907417cc;
          }
          FUN_ffff00009077de54(s_checksum_of_area__d_mismatch_s___ffff0000907ff22a,lVar11);
          iVar4 = *(int *)(&DAT_ffff00009086f3d8 + lVar11 * 4);
        }
        else {
          FUN_ffff00009077de54(s_ENV_SIG_of_area__d_not_found__it_ffff0000907e7abb,lVar11);
          iVar4 = *(int *)(&DAT_ffff00009086f3d8 + lVar11 * 4);
        }
        if (iVar4 == 0) {
          FUN_ffff00009077eb88(lVar1,0,0x4000);
        }
      }
    }
LAB_ffff0000907417cc:
    uVar12 = 0x24000;
    lVar11 = 1;
    bVar3 = !bVar7;
    bVar7 = false;
    if (bVar3) {
      return;
    }
  } while( true );
}


