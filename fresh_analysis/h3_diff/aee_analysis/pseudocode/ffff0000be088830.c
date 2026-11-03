
int FUN_ffff0000be088830(char *param_1,long param_2,undefined4 param_3,long *param_4)

{
  int iVar1;
  int iVar2;
  int iVar3;
  char cVar4;
  ulong uVar5;
  char local_50 [32];
  
  local_50[0x10] = '\0';
  local_50[0x11] = '\0';
  local_50[0x12] = '\0';
  local_50[0x13] = '\0';
  local_50[0x14] = '\0';
  local_50[0x15] = '\0';
  local_50[0x16] = '\0';
  local_50[0x17] = '\0';
  local_50[0x18] = '\0';
  local_50[0x19] = '\0';
  local_50[0x1a] = '\0';
  local_50[0x1b] = '\0';
  local_50[0x1c] = '\0';
  local_50[0x1d] = '\0';
  local_50[0x1e] = '\0';
  local_50[0x1f] = '\0';
  local_50[0] = '\0';
  local_50[1] = '\0';
  local_50[2] = '\0';
  local_50[3] = '\0';
  local_50[4] = '\0';
  local_50[5] = '\0';
  local_50[6] = '\0';
  local_50[7] = '\0';
  local_50[8] = '\0';
  local_50[9] = '\0';
  local_50[10] = '\0';
  local_50[0xb] = '\0';
  local_50[0xc] = '\0';
  local_50[0xd] = '\0';
  local_50[0xe] = '\0';
  local_50[0xf] = '\0';
  if (param_1 == (char *)0x0) {
    return 0x100006;
  }
  if (param_2 == 0) {
    return 0x100006;
  }
  if (param_4 == (long *)0x0) {
    return 0x100006;
  }
  iVar1 = FUN_ffff0000be0877b0(param_2,param_3,&DAT_ffff0000be1b6b08);
  if (iVar1 != 0) {
    return iVar1;
  }
  if (DAT_ffff0000be1b6b08 == 1) {
    cVar4 = *param_1;
    if (cVar4 != '\0') {
      uVar5 = 0;
      do {
        local_50[uVar5] = cVar4;
        cVar4 = param_1[uVar5 + 1];
        if (0x1e < uVar5) break;
        uVar5 = uVar5 + 1;
      } while (cVar4 != '\0');
      if (cVar4 != '\0') {
        FUN_ffff0000be05fd98(s_Image_name_length_is_too_long_ffff0000be0c6d1d);
      }
    }
    iVar1 = 0x10100f;
    iVar2 = FUN_ffff0000be060e5c(&DAT_ffff0000be1b6b1c);
    iVar3 = FUN_ffff0000be060e5c(local_50);
    if ((iVar2 != iVar3) ||
       (iVar2 = FUN_ffff0000be060778(&DAT_ffff0000be1b6b1c,local_50,iVar2), iVar2 != 0))
    goto LAB_ffff0000be088928;
    FUN_ffff0000be05fd98(s_Image_name_matched_ffff0000be0b4c3d);
  }
  iVar1 = 0;
LAB_ffff0000be088928:
  *param_4 = *param_4 + (ulong)DAT_ffff0000be1b6b0c + (ulong)DAT_ffff0000be1b6b14;
  return iVar1;
}


