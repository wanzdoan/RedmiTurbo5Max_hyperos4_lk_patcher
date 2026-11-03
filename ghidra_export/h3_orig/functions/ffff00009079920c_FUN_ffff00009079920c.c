// ffff00009079920c  size=540  FUN_ffff00009079920c

int FUN_ffff00009079920c
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
  
  iVar5 = FUN_ffff000090798ff0();
  if (iVar5 == 0) {
    uVar10 = *(ulong *)(*(long *)((long)param_1 * 8 + -0xffff6f6f5648) + 0x10);
    if ((param_5 < uVar10 || uVar10 * 0xff < param_7) || ((param_2 == 0 && (param_3 != 0)))) {
      iVar5 = 0x10;
    }
    else {
      if (param_6 == 0) {
                    /* WARNING: Subroutine does not return */
        FUN_ffff000090798f30
                  (s_out____NULL_ffff0000907f8ef4,
                   s_platform_mediatek_mt6991_sboot_c_ffff0000907e5684,0x41);
      }
      lVar2 = uVar10 + param_3;
      lVar1 = lVar2 + 1;
      lVar6 = FUN_ffff00009077d24c();
      if (lVar6 == 0) {
        iVar5 = 0xd;
      }
      else {
        if (param_2 != 0) {
          FUN_ffff00009077e858(lVar6 + uVar10,param_2,param_3);
        }
        lVar11 = lVar6 + uVar10;
        local_68 = uVar10;
        if (param_7 <= uVar10) {
          local_68 = param_7;
        }
        *(undefined1 *)(lVar6 + lVar2) = 1;
        iVar5 = FUN_ffff00009079996c
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
            FUN_ffff00009077e858(lVar6,param_6 + ((ulong)bVar7 - 1) * uVar10,uVar10);
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
            iVar5 = FUN_ffff00009079996c
                              (param_1,param_4,param_5,lVar11,lVar3,param_6 + uVar9,&local_68);
            lVar12 = lVar3;
            bVar8 = bVar7;
            bVar7 = bVar7 + 1;
          } while (iVar5 == 0);
        }
        FUN_ffff00009079be14(lVar6,lVar1);
        thunk_FUN_ffff0000907c2ad8(lVar6);
      }
    }
  }
  return iVar5;
}


