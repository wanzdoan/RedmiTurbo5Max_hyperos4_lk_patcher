// ffff0000907419fc  size=480  FUN_ffff0000907419fc

void FUN_ffff0000907419fc(void)

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
    lVar1 = lVar10 + -0xffff6f790b88;
    *(undefined4 *)(&DAT_ffff00009086f470 + lVar11 * 4) = 0;
    FUN_ffff00009077ee50(lVar1,0,0x4000);
    *(long *)(&DAT_ffff000090877480 + lVar11 * 0x20) = lVar10 + -0xffff6f790b80;
    lVar5 = FUN_ffff00009077c028(&DAT_ffff0000907d9482);
    if (lVar5 != 0) {
      lVar6 = FUN_ffff00009077c0bc(lVar5,lVar1,uVar12,0x4000);
      FUN_ffff00009077bfac(lVar5);
      if (lVar6 == 0x4000) {
        lVar5 = lVar11 * 0x20;
        FUN_ffff00009077eb20(lVar5 + -0xffff6f788b88,lVar1,8);
        FUN_ffff00009077eb20(lVar5 + -0xffff6f788b78,lVar10 + -0xffff6f78cb94,8);
        *(undefined1 *)(lVar5 + -0xffff6f788b81) = 0;
        *(undefined1 *)(lVar5 + -0xffff6f788b71) = 0;
        iVar4 = FUN_ffff00009077f0ac(lVar5 + -0xffff6f788b88,s_ENV_v1_ffff0000907d41a2);
        if ((iVar4 == 0) &&
           (iVar4 = FUN_ffff00009077f0ac(lVar5 + -0xffff6f788b78,s_ENV_v1_ffff0000907d41a2),
           iVar4 == 0)) {
          lVar5 = *(long *)(&DAT_ffff000090877480 + lVar11 * 0x20);
          iVar4 = 0;
          iVar8 = 0;
          iVar2 = *(int *)(&DAT_ffff000090873474 + lVar10);
          lVar10 = 0x3fec;
          *(int *)(lVar11 * 0x20 + -0xffff6f788b70) = iVar2;
          pbVar9 = (byte *)(lVar5 + 1);
          do {
            lVar10 = lVar10 + -2;
            iVar4 = iVar4 + (uint)pbVar9[-1];
            iVar8 = iVar8 + (uint)*pbVar9;
            pbVar9 = pbVar9 + 2;
          } while (lVar10 != 0);
          if (iVar8 + iVar4 == iVar2) {
            FUN_ffff00009077e11c(s_ENV_of_area__d_initialize_succes_ffff0000907e3034,lVar11);
            *(undefined4 *)(&DAT_ffff00009086f470 + lVar11 * 4) = 1;
            goto LAB_ffff000090741a64;
          }
          FUN_ffff00009077e11c(s_checksum_of_area__d_mismatch_s___ffff0000907ff3d2,lVar11);
          iVar4 = *(int *)(&DAT_ffff00009086f470 + lVar11 * 4);
        }
        else {
          FUN_ffff00009077e11c(s_ENV_SIG_of_area__d_not_found__it_ffff0000907e7c16,lVar11);
          iVar4 = *(int *)(&DAT_ffff00009086f470 + lVar11 * 4);
        }
        if (iVar4 == 0) {
          FUN_ffff00009077ee50(lVar1,0,0x4000);
        }
      }
    }
LAB_ffff000090741a64:
    uVar12 = 0x24000;
    lVar11 = 1;
    bVar3 = !bVar7;
    bVar7 = false;
    if (bVar3) {
      return;
    }
  } while( true );
}


