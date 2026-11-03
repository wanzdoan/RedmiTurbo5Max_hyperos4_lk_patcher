
int FUN_ffff0000b805bd20(long param_1,long param_2,ulong *param_3)

{
  ulong uVar1;
  int iVar2;
  undefined *puVar3;
  int iVar4;
  long lVar5;
  long lVar6;
  long lVar7;
  ulong uVar8;
  ulong uVar9;
  long lVar10;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_hmac____NULL_ffff0000b8086be2,s_platform_mediatek_mt6991_sboot_c_ffff0000b809dfc7,
               0x22);
  }
  if (param_2 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b805b6c8
              (s_out____NULL_ffff0000b809bfca,s_platform_mediatek_mt6991_sboot_c_ffff0000b809dfc7,
               0x23);
  }
  iVar2 = *(int *)(param_1 + 0x70);
  iVar4 = FUN_ffff0000b805b788(iVar2);
  puVar3 = PTR_DAT_ffff0000b81087e0;
  if (iVar4 == 0) {
    lVar10 = (long)iVar2;
    uVar1 = *(ulong *)(*(long *)(PTR_DAT_ffff0000b81087e0 + lVar10 * 8) + 0x10);
    lVar5 = FUN_ffff0000b804bccc
                      (*(undefined8 *)(*(long *)(PTR_DAT_ffff0000b81087e0 + lVar10 * 8) + 0x18));
    lVar6 = FUN_ffff0000b804bccc(uVar1);
    if (lVar5 == 0 || lVar6 == 0) {
      if (lVar5 != 0) {
        thunk_FUN_ffff0000b8082558(lVar5);
      }
      iVar4 = 0xd;
      lVar5 = lVar6;
      if (lVar6 == 0) {
        return 0xd;
      }
    }
    else {
      iVar4 = (**(code **)(*(long *)(puVar3 + lVar10 * 8) + 0xb8))(param_1,lVar6);
      if (iVar4 == 0) {
        lVar7 = *(long *)(puVar3 + lVar10 * 8);
        if (*(long *)(lVar7 + 0x18) != 0) {
          uVar9 = 0;
          do {
            *(byte *)(lVar5 + uVar9) = *(byte *)(param_1 + 0xe8 + uVar9) ^ 0x5c;
            uVar9 = uVar9 + 1;
          } while (uVar9 < *(ulong *)(lVar7 + 0x18));
        }
        iVar4 = (**(code **)(lVar7 + 0xa8))(param_1);
        if ((((iVar4 == 0) &&
             (iVar4 = (**(code **)(*(long *)(puVar3 + lVar10 * 8) + 0xb0))
                                (param_1,lVar5,
                                 *(undefined8 *)(*(long *)(puVar3 + lVar10 * 8) + 0x18)), iVar4 == 0
             )) && (iVar4 = (**(code **)(*(long *)(puVar3 + lVar10 * 8) + 0xb0))
                                      (param_1,lVar6,uVar1), iVar4 == 0)) &&
           (iVar4 = (**(code **)(*(long *)(puVar3 + lVar10 * 8) + 0xb8))(param_1,lVar5), iVar4 == 0)
           ) {
          uVar9 = 0;
          uVar8 = uVar9;
          if (uVar1 != 0) {
            do {
              uVar8 = uVar9;
              if (*param_3 <= uVar9) break;
              *(undefined1 *)(param_2 + uVar9) = *(undefined1 *)(lVar5 + uVar9);
              uVar9 = uVar9 + 1;
              uVar8 = uVar1;
            } while (uVar1 != uVar9);
          }
          iVar4 = 0;
          *param_3 = uVar8;
        }
      }
      FUN_ffff0000b805e5ac(lVar6,uVar1);
      FUN_ffff0000b805e5ac(lVar5,uVar1);
      FUN_ffff0000b805e5ac(param_1,0x178);
      thunk_FUN_ffff0000b8082558(lVar6);
    }
    thunk_FUN_ffff0000b8082558(lVar5);
  }
  return iVar4;
}


