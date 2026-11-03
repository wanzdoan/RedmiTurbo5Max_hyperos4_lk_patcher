
int FUN_ffff0000be0185b4(long param_1,int param_2)

{
  long lVar1;
  byte bVar2;
  int iVar3;
  ulong uVar4;
  
  if (param_1 == 0) {
    iVar3 = -100;
  }
  else {
    if (*DAT_ffff0000be108808 != '\0') {
      bVar2 = FUN_ffff0000be0184e4(param_1,param_2);
      DAT_ffff0000be108800 = bVar2 & 1;
      if ((bVar2 & 1) == 0) {
        FUN_ffff0000be015e08(&DAT_ffff0000be1087d0,*(undefined8 *)(DAT_ffff0000be108808 + 0x18));
        uVar4 = *(ulong *)(DAT_ffff0000be108808 + 0x10);
      }
      else {
        FUN_ffff0000be01807c(&DAT_ffff0000be1087a0);
        uVar4 = FUN_ffff0000be018504(param_1);
      }
      if (*(ulong *)(*(long *)(DAT_ffff0000be108808 + 0x18) + 8) < uVar4) {
        FUN_ffff0000be05fd98(s_size_too_large__space_small__ffff0000be0aa22f);
        FUN_ffff0000be05fd98
                  (s_image_length_0x_llx__partition_m_ffff0000be0b3f07,uVar4,
                   *(undefined8 *)(*(long *)(DAT_ffff0000be108808 + 0x18) + 8));
        return -0x65;
      }
      *DAT_ffff0000be108808 = '\0';
    }
    if (DAT_ffff0000be108800 == 0) {
      if (param_2 == 0) {
        FUN_ffff0000be015e44(&DAT_ffff0000be1087d0);
        iVar3 = DAT_ffff0000be1087d0;
      }
      else {
        FUN_ffff0000be015e50(&DAT_ffff0000be1087d0,param_1,param_2);
        iVar3 = DAT_ffff0000be1087d0;
      }
    }
    else if (param_2 == 0) {
      FUN_ffff0000be0180c0(&DAT_ffff0000be1087a0);
      iVar3 = DAT_ffff0000be1087a0;
    }
    else {
      uVar4 = (long)*(int *)(DAT_ffff0000be108810 + 0xf8) ^ 1;
      FUN_ffff0000be0180cc(&DAT_ffff0000be1087a0,param_1,param_2);
      iVar3 = DAT_ffff0000be1087a0;
      if (DAT_ffff0000be1087a0 == 0) {
        *(undefined4 *)(DAT_ffff0000be108810 + (long)(int)uVar4 * 0x78 + 0x18) = 0;
      }
      else if (DAT_ffff0000be1087a0 == 100) {
        lVar1 = uVar4 * 0x78;
        FUN_ffff0000be0607b0
                  (*(long *)(DAT_ffff0000be108810 + lVar1 + 8) +
                   (ulong)(0x1000 - DAT_ffff0000be1087a8),DAT_ffff0000be1087b0);
        DAT_ffff0000be1087a0 = 0;
        *(int *)(DAT_ffff0000be108810 + lVar1 + 0x18) = DAT_ffff0000be1087a8;
        iVar3 = 0;
      }
    }
  }
  return iVar3;
}


