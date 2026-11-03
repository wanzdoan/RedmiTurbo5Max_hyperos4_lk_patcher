// ffff000090734d24  size=240  FUN_ffff000090734d24

undefined8 FUN_ffff000090734d24(long param_1,uint *param_2)

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
    iVar1 = FUN_ffff00009077ede4(param_1,s_userdata_ffff0000907ea542);
    if (iVar1 == 0) {
      iVar1 = FUN_ffff00009077ede4(&DAT_ffff000090800a04,&DAT_ffff0000907e6178);
      uVar5 = 1;
      if (iVar1 != 0) {
        uVar5 = 2;
      }
      uVar2 = 0;
      *param_2 = uVar5;
    }
    else {
      lVar3 = FUN_ffff00009077bd60(param_1);
      if (lVar3 == 0) {
        FUN_ffff00009077de54(s_Partition___s__is_not_exist__ffff0000907ea8c0,param_1);
        uVar2 = 0xffffffed;
      }
      else {
        lVar4 = FUN_ffff00009077bdf4(lVar3,&local_14,0x438,2);
        if (lVar4 == 2) {
          *param_2 = (uint)(local_14 == 0xef53);
          FUN_ffff00009077bce4(lVar3);
          uVar2 = 0;
        }
        else {
          FUN_ffff00009077bce4(lVar3);
          uVar2 = 0xfffffffb;
        }
      }
    }
  }
  return uVar2;
}


