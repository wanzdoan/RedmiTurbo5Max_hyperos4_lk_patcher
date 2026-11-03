
int FUN_ffff0000be09ec54(undefined8 param_1,undefined8 param_2,ushort *param_3)

{
  long lVar1;
  ushort uVar2;
  bool bVar3;
  int iVar4;
  ulong *puVar5;
  ulong *puVar6;
  ulong *puVar7;
  ulong uVar8;
  ulong uVar9;
  uint uVar10;
  long lVar11;
  long lVar12;
  ulong local_118;
  long local_110;
  byte *local_108;
  ulong local_100;
  long local_f8;
  byte *local_f0;
  ulong local_e8;
  long local_e0;
  byte *local_d8;
  ulong local_d0;
  long local_c8;
  long local_c0;
  ulong local_b8;
  long local_b0;
  byte *local_a8;
  ulong local_a0;
  long local_98;
  byte *local_90;
  ulong local_88;
  long local_80;
  byte *local_78;
  ulong local_70;
  long local_68;
  long local_60;
  ulong local_58;
  ulong local_50;
  uint *local_48;
  
  uVar8 = *(ulong *)(param_3 + 4);
  do {
    uVar9 = uVar8;
    uVar8 = uVar9 - 1;
    if (uVar8 == 0xffffffffffffffff) {
      return -4;
    }
  } while ((*(uint **)(param_3 + 8))[uVar8] == 0);
  uVar2 = *param_3;
  uVar10 = (uint)(short)uVar2;
  if (uVar9 < 2) {
    if ((short)uVar2 < 0) {
      return -4;
    }
    if (**(uint **)(param_3 + 8) < 2) {
      return -4;
    }
    uVar10 = (uint)uVar2;
  }
  if ((int)uVar10 < 1) {
    return -4;
  }
  local_68 = 0;
  local_60 = 0;
  local_80 = 0;
  local_78 = (byte *)0x0;
  bVar3 = DAT_ffff0000be1b7e68 != 0;
  local_98 = 0;
  local_90 = (byte *)0x0;
  local_b0 = 0;
  local_a8 = (byte *)0x0;
  local_70 = (ulong)CONCAT12(bVar3,1);
  local_88 = (ulong)CONCAT12(bVar3,1);
  local_a0 = (ulong)CONCAT12(bVar3,1);
  local_b8 = (ulong)CONCAT12(bVar3,1);
  local_58 = (ulong)CONCAT12(bVar3,1);
  local_50 = 0;
  local_48 = (uint *)0x0;
  local_d0 = (ulong)CONCAT12(bVar3,1);
  local_c8 = 0;
  local_c0 = 0;
  local_e8 = (ulong)CONCAT12(bVar3,1);
  local_e0 = 0;
  local_d8 = (byte *)0x0;
  local_100 = (ulong)CONCAT12(bVar3,1);
  local_f8 = 0;
  local_f0 = (byte *)0x0;
  local_118 = (ulong)CONCAT12(bVar3,1);
  local_110 = 0;
  local_108 = (byte *)0x0;
  iVar4 = FUN_ffff0000be09dfc0(&local_58,param_2,param_3);
  uVar8 = local_50;
  if (iVar4 != 0) goto LAB_ffff0000be09ede4;
  do {
    uVar9 = uVar8;
    uVar8 = uVar9 - 1;
    if (uVar8 == 0xffffffffffffffff) goto LAB_ffff0000be09ede0;
  } while (local_48[uVar8] == 0);
  uVar10 = (uint)(short)(ushort)local_58;
  if (1 < uVar9) goto LAB_ffff0000be09eddc;
  uVar10 = (uint)(ushort)local_58;
  if ((short)(ushort)local_58 < 0) {
LAB_ffff0000be09ede0:
    iVar4 = -0xe;
  }
  else {
    if (*local_48 < 2) {
      if (*local_48 == 0) {
        uVar10 = -(uint)(ushort)local_58;
        goto LAB_ffff0000be09eddc;
      }
    }
    else {
LAB_ffff0000be09eddc:
      if (uVar10 != 0) goto LAB_ffff0000be09ede0;
    }
    iVar4 = FUN_ffff0000be09de20(&local_70,param_2,param_3);
    if (((((iVar4 == 0) && (iVar4 = FUN_ffff0000be09bf10(&local_88,&local_70), iVar4 == 0)) &&
         (iVar4 = FUN_ffff0000be09bf10(&local_d0,param_3), iVar4 == 0)) &&
        ((iVar4 = FUN_ffff0000be09bf10(&local_e8,param_3), iVar4 == 0 &&
         (iVar4 = FUN_ffff0000be09c1d8(&local_a0,1), iVar4 == 0)))) &&
       ((iVar4 = FUN_ffff0000be09c1d8(&local_b8,0), iVar4 == 0 &&
        ((iVar4 = FUN_ffff0000be09c1d8(&local_100,0), iVar4 == 0 &&
         (iVar4 = FUN_ffff0000be09c1d8(&local_118,1), iVar4 == 0)))))) {
      do {
        while ((*local_78 & 1) == 0) {
          iVar4 = FUN_ffff0000be09c798(&local_88,1);
          if (((iVar4 != 0) ||
              ((((*local_90 & 1) != 0 || ((*local_a8 & 1) != 0)) &&
               ((iVar4 = FUN_ffff0000be09cef8(&local_a0,&local_a0,&local_d0), iVar4 != 0 ||
                (iVar4 = FUN_ffff0000be09d008(&local_b8,&local_b8,&local_70), iVar4 != 0)))))) ||
             ((iVar4 = FUN_ffff0000be09c798(&local_a0,1), iVar4 != 0 ||
              (iVar4 = FUN_ffff0000be09c798(&local_b8,1), iVar4 != 0)))) goto LAB_ffff0000be09ede4;
        }
        while ((*local_d8 & 1) == 0) {
          iVar4 = FUN_ffff0000be09c798(&local_e8,1);
          if ((iVar4 != 0) ||
             (((((*local_f0 & 1) != 0 || ((*local_108 & 1) != 0)) &&
               ((iVar4 = FUN_ffff0000be09cef8(&local_100,&local_100,&local_d0), iVar4 != 0 ||
                (iVar4 = FUN_ffff0000be09d008(&local_118,&local_118,&local_70), iVar4 != 0)))) ||
              ((iVar4 = FUN_ffff0000be09c798(&local_100,1), iVar4 != 0 ||
               (iVar4 = FUN_ffff0000be09c798(&local_118,1), iVar4 != 0))))))
          goto LAB_ffff0000be09ede4;
        }
        iVar4 = FUN_ffff0000be09c8f4(&local_88,&local_e8);
        if (iVar4 < 0) {
          iVar4 = FUN_ffff0000be09d008(&local_e8,&local_e8,&local_88);
          if ((iVar4 != 0) ||
             (iVar4 = FUN_ffff0000be09d008(&local_100,&local_100,&local_a0), iVar4 != 0)) break;
          puVar5 = &local_118;
          puVar6 = &local_118;
          puVar7 = &local_b8;
        }
        else {
          iVar4 = FUN_ffff0000be09d008(&local_88,&local_88,&local_e8);
          if ((iVar4 != 0) ||
             (iVar4 = FUN_ffff0000be09d008(&local_a0,&local_a0,&local_100), iVar4 != 0)) break;
          puVar5 = &local_b8;
          puVar6 = &local_b8;
          puVar7 = &local_118;
        }
        iVar4 = FUN_ffff0000be09d008(puVar5,puVar6,puVar7);
        lVar11 = local_80;
        if (iVar4 != 0) break;
        do {
          lVar12 = local_f8;
          if (lVar11 == 0) goto LAB_ffff0000be09f1c4;
          lVar1 = lVar11 * 4;
          lVar11 = lVar11 + -1;
        } while (*(int *)(local_78 + lVar1 + -4) == 0);
        if ((short)local_88 == 0) goto LAB_ffff0000be09f1c4;
      } while( true );
    }
  }
  goto LAB_ffff0000be09ede4;
  while( true ) {
    if (-1 < (short)local_100) goto LAB_ffff0000be09f1fc;
    iVar4 = FUN_ffff0000be09cef8(&local_100,&local_100,param_3);
    lVar12 = local_f8;
    if (iVar4 != 0) break;
LAB_ffff0000be09f1c4:
    do {
      if (lVar12 == 0) goto LAB_ffff0000be09f1fc;
      lVar11 = lVar12 * 4;
      lVar12 = lVar12 + -1;
    } while (*(int *)(local_f0 + lVar11 + -4) == 0);
  }
  goto LAB_ffff0000be09ede4;
  while (iVar4 = FUN_ffff0000be09d008(&local_100,&local_100,param_3), iVar4 == 0) {
LAB_ffff0000be09f1fc:
    iVar4 = FUN_ffff0000be09c8f4(&local_100,param_3);
    if (iVar4 < 0) {
      iVar4 = FUN_ffff0000be09bf10(param_1,&local_100);
      break;
    }
  }
LAB_ffff0000be09ede4:
  if (local_60 != 0) {
    FUN_ffff0000be0a174c(local_60,local_68 << 2);
    thunk_FUN_ffff0000be09937c(local_60);
  }
  local_68 = 0;
  local_60 = 0;
  local_70 = CONCAT62(local_70._2_6_,1);
  if (local_78 != (byte *)0x0) {
    FUN_ffff0000be0a174c(local_78,local_80 << 2);
    thunk_FUN_ffff0000be09937c(local_78);
  }
  local_88 = CONCAT62(local_88._2_6_,1);
  local_80 = 0;
  local_78 = (byte *)0x0;
  if (local_90 != (byte *)0x0) {
    FUN_ffff0000be0a174c(local_90,local_98 << 2);
    thunk_FUN_ffff0000be09937c(local_90);
  }
  local_a0 = CONCAT62(local_a0._2_6_,1);
  local_98 = 0;
  local_90 = (byte *)0x0;
  if (local_a8 != (byte *)0x0) {
    FUN_ffff0000be0a174c(local_a8,local_b0 << 2);
    thunk_FUN_ffff0000be09937c(local_a8);
  }
  local_b8 = CONCAT62(local_b8._2_6_,1);
  local_b0 = 0;
  local_a8 = (byte *)0x0;
  if (local_48 != (uint *)0x0) {
    FUN_ffff0000be0a174c(local_48,local_50 << 2);
    thunk_FUN_ffff0000be09937c(local_48);
  }
  local_58 = CONCAT62(local_58._2_6_,1);
  local_50 = 0;
  local_48 = (uint *)0x0;
  if (local_c0 != 0) {
    FUN_ffff0000be0a174c(local_c0,local_c8 << 2);
    thunk_FUN_ffff0000be09937c(local_c0);
  }
  local_d0 = CONCAT62(local_d0._2_6_,1);
  local_c8 = 0;
  local_c0 = 0;
  if (local_d8 != (byte *)0x0) {
    FUN_ffff0000be0a174c(local_d8,local_e0 << 2);
    thunk_FUN_ffff0000be09937c(local_d8);
  }
  local_e8 = CONCAT62(local_e8._2_6_,1);
  local_e0 = 0;
  local_d8 = (byte *)0x0;
  if (local_f0 != (byte *)0x0) {
    FUN_ffff0000be0a174c(local_f0,local_f8 << 2);
    thunk_FUN_ffff0000be09937c(local_f0);
  }
  local_100 = CONCAT62(local_100._2_6_,1);
  local_f8 = 0;
  local_f0 = (byte *)0x0;
  if (local_108 != (byte *)0x0) {
    FUN_ffff0000be0a174c(local_108,local_110 << 2);
    thunk_FUN_ffff0000be09937c(local_108);
  }
  return iVar4;
}


