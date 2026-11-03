
undefined8 FUN_ffff000090734fbc(long param_1,uint *param_2)

{
  int iVar1;
  undefined8 uVar2;
  long lVar3;
  long lVar4;
  uint uVar5;
  int local_14;
  
  uVar2 = 0xffffffea;
  if ((param_1 != 0) && (param_2 != (uint *)0x0)) {
    local_14 = 0;
    iVar1 = FUN_ffff00009077f0ac(param_1,s_userdata_ffff0000907ea6b3);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077f0ac(&DAT_ffff000090800be0,&DAT_ffff0000907e62a3);
      uVar5 = 1;
      if (iVar1 != 0) {
        uVar5 = 2;
      }
      uVar2 = 0;
      *param_2 = uVar5;
    }
    else {
      lVar3 = FUN_ffff00009077c028(param_1);
      if (lVar3 == 0) {
        FUN_ffff00009077e11c(s_Partition___s__is_not_exist__ffff0000907eaa5a,param_1);
        uVar2 = 0xffffffed;
      }
      else {
        lVar4 = FUN_ffff00009077c0bc(lVar3,&local_14,0x438,2);
        if (lVar4 == 2) {
          *param_2 = (uint)(local_14 == 0xef53);
          FUN_ffff00009077bfac(lVar3);
          uVar2 = 0;
        }
        else {
          FUN_ffff00009077bfac(lVar3);
          uVar2 = 0xfffffffb;
        }
      }
    }
  }
  return uVar2;
}


