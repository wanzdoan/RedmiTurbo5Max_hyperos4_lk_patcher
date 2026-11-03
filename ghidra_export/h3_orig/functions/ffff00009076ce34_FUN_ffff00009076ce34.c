// ffff00009076ce34  size=228  FUN_ffff00009076ce34

uint FUN_ffff00009076ce34(undefined8 param_1,uint param_2)

{
  bool bVar1;
  int iVar2;
  int iVar3;
  uint local_38;
  uint local_34;
  
  iVar3 = 1;
  while( true ) {
    local_38 = 0;
    if ((-1 < (int)param_2) &&
       ((((param_2 & 0x80000003) != 0 ||
         (local_34 = param_2, iVar2 = FUN_ffff00009076ca80(param_1,param_2,&local_34), iVar2 != 1))
        || (local_38 = local_34, (int)local_34 < 0)))) {
      return 0xffffffff;
    }
    while( true ) {
      while (param_2 = local_38, iVar2 = FUN_ffff00009076ca80(param_1,local_38,&local_38),
            iVar2 == 2) {
        if (iVar3 < 1) {
          return 0xffffffff;
        }
        iVar3 = iVar3 + -1;
      }
      if (iVar2 == 1) break;
      if (iVar2 == 9) {
        return 0xffffffff;
      }
    }
    if ((int)param_2 < 0) {
      return 0xffffffff;
    }
    if (iVar3 < 0) break;
    bVar1 = iVar3 == 0;
    iVar3 = iVar3 + 1;
    if (bVar1) {
      return param_2;
    }
  }
  return 0xffffffff;
}


