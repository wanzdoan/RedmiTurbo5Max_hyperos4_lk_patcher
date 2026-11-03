// ffff0000907abb30  size=316  FUN_ffff0000907abb30

int FUN_ffff0000907abb30(byte param_1)

{
  int iVar1;
  int iVar2;
  uint uVar3;
  uint uVar4;
  
  iVar1 = FUN_ffff0000907abc6c();
  if (iVar1 == 0) {
    iVar1 = 0x104000;
    uVar3 = *(uint *)(DAT_ffff000090839a80 + 0x454);
    uVar4 = *(uint *)(DAT_ffff000090839a90 + 0x70);
    if (uVar3 == *(uint *)(DAT_ffff000090839a90 + 0x70)) {
      uVar4 = uVar3;
      if (uVar3 < (byte)(&DAT_ffff000090837e88)[(ulong)param_1 * 2]) {
        iVar1 = 0x104001;
      }
      else if (uVar3 < 8) {
        if (uVar3 < (byte)(&DAT_ffff000090837e89)[(ulong)param_1 * 2]) {
          iVar1 = 0;
          (&DAT_ffff000090837e89)[(ulong)param_1 * 2] = (byte)uVar3;
        }
        else {
          iVar1 = 0;
        }
      }
      else {
        iVar1 = 0x104002;
      }
    }
  }
  else {
    uVar3 = 0;
    uVar4 = 0;
  }
  iVar2 = FUN_ffff0000907ad53c();
  if (iVar2 != 0) {
    FUN_ffff00009077de54
              (s___s__img_sub_group_ver_check___g_ffff00009080323f,&DAT_ffff0000907f20e7,param_1,
               uVar3,uVar4,(&DAT_ffff000090837e88)[(ulong)param_1 * 2]);
  }
  iVar2 = FUN_ffff0000907ad53c();
  if (iVar1 == 0) {
    if (iVar2 != 0) {
      FUN_ffff00009077de54(s____ok_ffff000090801938);
    }
  }
  else if (iVar2 != 0) {
    FUN_ffff00009077de54(s____fail__0x_x__ffff00009080328d,iVar1);
  }
  return iVar1;
}


