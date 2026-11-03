
int FUN_ffff0000be075b94
              (int param_1,long param_2,long param_3,undefined8 param_4,ulong param_5,long param_6,
              ulong param_7)

{
  long lVar1;
  long lVar2;
  long lVar3;
  long lVar4;
  int iVar5;
  long lVar6;
  byte bVar7;
  byte bVar8;
  ulong uVar9;
  ulong uVar10;
  long lVar11;
  long lVar12;
  ulong local_68;
  
  iVar5 = FUN_ffff0000be075978();
  if (iVar5 == 0) {
    uVar10 = *(ulong *)((&DAT_ffff0000be1b4f88)[param_1] + 0x10);
    if ((param_5 < uVar10 || uVar10 * 0xff < param_7) || ((param_2 == 0 && (param_3 != 0)))) {
      iVar5 = 0x10;
    }
    else {
      if (param_6 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff0000be0758b8
                  (s_out____NULL_ffff0000be0c018a,
                   s_platform_mediatek_mt6991_sboot_c_ffff0000be0b2854,0x41);
      }
      lVar2 = uVar10 + param_3;
      lVar1 = lVar2 + 1;
      lVar6 = FUN_ffff0000be05f1bc();
      if (lVar6 == 0) {
        iVar5 = 0xd;
      }
      else {
        if (param_2 != 0) {
          FUN_ffff0000be0607b0(lVar6 + uVar10,param_2,param_3);
        }
        lVar11 = lVar6 + uVar10;
        local_68 = uVar10;
        if (param_7 <= uVar10) {
          local_68 = param_7;
        }
        *(undefined1 *)(lVar6 + lVar2) = 1;
        iVar5 = FUN_ffff0000be0762f4
                          (param_1,param_4,param_5,lVar11,lVar1 - uVar10,param_6,&local_68);
        if (iVar5 == 0) {
          uVar9 = 0;
          lVar12 = lVar1 - uVar10;
          bVar8 = 0;
          bVar7 = 1;
          do {
            uVar9 = local_68 + uVar9;
            if (param_7 <= uVar9) {
              iVar5 = 0;
              break;
            }
            FUN_ffff0000be0607b0(lVar6,param_6 + ((ulong)bVar7 - 1) * uVar10,uVar10);
            lVar3 = lVar1;
            lVar4 = lVar6;
            if (bVar8 != 0) {
              lVar3 = lVar12;
              lVar4 = lVar11;
            }
            lVar11 = lVar4;
            local_68 = uVar10;
            if (param_7 - uVar9 <= uVar10) {
              local_68 = param_7 - uVar9;
            }
            *(byte *)(lVar6 + lVar2) = bVar7 + 1;
            iVar5 = FUN_ffff0000be0762f4
                              (param_1,param_4,param_5,lVar11,lVar3,param_6 + uVar9,&local_68);
            lVar12 = lVar3;
            bVar8 = bVar7;
            bVar7 = bVar7 + 1;
          } while (iVar5 == 0);
        }
        FUN_ffff0000be07879c(lVar6,lVar1);
        thunk_FUN_ffff0000be09937c(lVar6);
      }
    }
  }
  return iVar5;
}


