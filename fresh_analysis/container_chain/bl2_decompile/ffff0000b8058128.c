
int FUN_ffff0000b8058128
              (long param_1,ulong param_2,ulong param_3,undefined1 param_4,undefined1 param_5,
              long param_6,uint *param_7)

{
  uint uVar1;
  int iVar2;
  char *pcVar3;
  undefined1 uVar4;
  ulong uVar5;
  
  uVar5 = param_2 & 0xffffffff;
  FUN_ffff0000b804c33c
            (s__UFS_Query__Enter__opcode_0x_x__i_ffff0000b80986b1,param_2,param_3,param_4,param_5,
             *param_7);
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff0000b804b830
              (s_platform_mediatek_common_ufs_ufs_ffff0000b80895e3,0x9a6,&DAT_ffff0000b808e5ee);
  }
  if (*param_7 - 0x100 < 0xffffff02) {
    FUN_ffff0000b804c33c(s__UFS_Query__ERROR__Buffer_size___ffff0000b8090494,*param_7,2,0xff);
  }
  else {
    if (param_6 != 0) {
      FUN_ffff0000b804d070(param_1 + 0x1c0,0,0x18);
      FUN_ffff0000b804d070(param_1 + 0x1e0,0,0x18);
      uVar1 = *param_7;
      *(char *)(param_1 + 0x1c4) = (char)param_2;
      *(char *)(param_1 + 0x1c5) = (char)param_3;
      *(undefined1 *)(param_1 + 0x1c6) = param_4;
      *(undefined1 *)(param_1 + 0x1c7) = param_5;
      *(long *)(param_1 + 0x1d8) = param_6;
      *(ushort *)(param_1 + 0x1ca) = (ushort)uVar1 >> 8 | (ushort)uVar1 << 8;
      if ((int)param_2 == 1) {
        uVar4 = 1;
      }
      else {
        if ((int)param_2 != 2) {
          pcVar3 = s__UFS_Query__ERROR__Invalid_opcod_ffff0000b809fe2c;
          goto LAB_ffff0000b8058258;
        }
        uVar4 = 0x81;
      }
      *(undefined1 *)(param_1 + 0x1c0) = uVar4;
      iVar2 = FUN_ffff0000b8057b60(param_1,1,5000);
      if (iVar2 == 0) {
        *(undefined8 *)(param_1 + 0x1d8) = 0;
        *param_7 = (uint)(*(ushort *)(param_1 + 0x1ea) >> 8) |
                   (*(ushort *)(param_1 + 0x1ea) & 0xff00ff) << 8;
        FUN_ffff0000b804c33c
                  (s__UFS_Query__Exit_SUCCESS__opcode_ffff0000b8087a83,uVar5,param_3 & 0xffffffff);
        return 0;
      }
      FUN_ffff0000b804c33c
                (s__UFS_Query__ERROR__Command_faile_ffff0000b80986fe,uVar5,param_3 & 0xffffffff,
                 iVar2);
      goto LAB_ffff0000b8058264;
    }
    pcVar3 = s__UFS_Query__ERROR__Null_buffer_f_ffff0000b8087a53;
LAB_ffff0000b8058258:
    FUN_ffff0000b804c33c(pcVar3,uVar5);
  }
  iVar2 = -1;
LAB_ffff0000b8058264:
  FUN_ffff0000b804c33c
            (s__UFS_Query__Exit_with_ERROR__d__o_ffff0000b80a0a0f,iVar2,uVar5,param_3 & 0xffffffff);
  return iVar2;
}


