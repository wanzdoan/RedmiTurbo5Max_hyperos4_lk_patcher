
undefined8 FUN_ffff0000907c12f0(byte *param_1,long param_2)

{
  byte bVar1;
  int iVar2;
  
  if (param_2 != 0) {
    iVar2 = 0;
    do {
      bVar1 = *param_1;
      if (iVar2 == 0) {
        if ((char)bVar1 < '\0') {
          if ((bVar1 & 0xe0) == 0xc0) {
            iVar2 = 1;
          }
          else if ((bVar1 & 0xf0) == 0xe0) {
            iVar2 = 2;
          }
          else {
            if ((bVar1 & 0xf8) != 0xf0) {
              return 0;
            }
            iVar2 = 3;
          }
        }
        else {
          iVar2 = 0;
        }
      }
      else {
        if ((bVar1 & 0xc0) != 0x80) {
          return 0;
        }
        iVar2 = iVar2 + -1;
      }
      param_2 = param_2 + -1;
      param_1 = param_1 + 1;
    } while (param_2 != 0);
    if (iVar2 != 0) {
      return 0;
    }
  }
  return 1;
}


