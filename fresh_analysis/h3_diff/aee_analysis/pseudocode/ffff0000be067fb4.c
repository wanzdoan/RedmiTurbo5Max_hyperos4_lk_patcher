
ulong FUN_ffff0000be067fb4(char *param_1,long param_2)

{
  long lVar1;
  ulong uVar2;
  ulong uVar3;
  undefined4 local_24;
  
  if (*param_1 == '\0') {
    uVar2 = 0;
  }
  else {
    uVar3 = 0;
    uVar2 = 0;
    local_24 = 0;
    do {
      FUN_ffff0000be060ae0(&local_24,0xff,4);
      lVar1 = 0;
      do {
        if (PTR_s_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef_ffff0000be0f2cb0[lVar1] == param_1[uVar3]) {
          local_24 = CONCAT31(local_24._1_3_,(char)lVar1);
        }
        lVar1 = lVar1 + 1;
      } while (lVar1 != 0x40);
      lVar1 = 0;
      do {
        if (PTR_s_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef_ffff0000be0f2cb0[lVar1] == param_1[uVar3 | 1]) {
          local_24._0_2_ = CONCAT11((char)lVar1,(char)local_24);
        }
        lVar1 = lVar1 + 1;
      } while (lVar1 != 0x40);
      lVar1 = 0;
      do {
        if (PTR_s_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef_ffff0000be0f2cb0[lVar1] == param_1[uVar3 | 2]) {
          local_24._0_3_ = CONCAT12((char)lVar1,(undefined2)local_24);
        }
        lVar1 = lVar1 + 1;
      } while (lVar1 != 0x40);
      lVar1 = 0;
      do {
        if (PTR_s_ABCDEFGHIJKLMNOPQRSTUVWXYZabcdef_ffff0000be0f2cb0[lVar1] == param_1[uVar3 | 3]) {
          local_24 = CONCAT13((char)lVar1,(undefined3)local_24);
        }
        lVar1 = lVar1 + 1;
      } while (lVar1 != 0x40);
      *(byte *)(param_2 + uVar2) = (char)local_24 << 2 | local_24._1_1_ >> 4 & 3;
      if (param_1[uVar3 | 2] == '=') {
        uVar2 = (ulong)((int)uVar2 + 1);
        break;
      }
      lVar1 = uVar2 + param_2;
      *(byte *)(lVar1 + 1) = local_24._1_1_ << 4 | local_24._2_1_ >> 2 & 0xf;
      if (param_1[uVar3 | 3] == '=') {
        uVar2 = (ulong)((int)uVar2 + 2);
        break;
      }
      uVar3 = uVar3 + 4;
      uVar2 = uVar2 + 3;
      *(byte *)(lVar1 + 2) = local_24._3_1_ & 0x3f | local_24._2_1_ << 6;
    } while (param_1[uVar3] != '\0');
  }
  return uVar2 & 0xffffffff;
}


