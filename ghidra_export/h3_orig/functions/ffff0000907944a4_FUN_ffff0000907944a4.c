// ffff0000907944a4  size=200  FUN_ffff0000907944a4

int FUN_ffff0000907944a4(int *param_1)

{
  int iVar1;
  long lVar2;
  int local_14;
  
  lVar2 = FUN_ffff000090792e88(0);
  if (param_1 != (int *)0x0) {
    local_14 = 0;
    FUN_ffff0000907175f4(lVar2 + 0x270,0xffffffff);
    iVar1 = (**(code **)(lVar2 + 0x220))(lVar2,3,0,0,0,&local_14);
    FUN_ffff00009071768c(lVar2 + 0x270);
    if (iVar1 != 0) {
      FUN_ffff00009077de54(s__UFS__err__read_ATTR_B_BOOT_LUN__ffff0000907d6e7d,iVar1);
      return iVar1;
    }
    if (local_14 - 1U < 2) {
      *param_1 = local_14;
      return 0;
    }
    FUN_ffff00009077de54(s__UFS__err__invalid_ATTR_B_BOOT_L_ffff0000907f55d6,*param_1);
  }
  return -1;
}


