// ffff0000907050f4  size=140  FUN_ffff0000907050f4

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

int FUN_ffff0000907050f4(undefined8 param_1)

{
  int iVar1;
  undefined8 uVar2;
  undefined4 local_24;
  
  local_24 = 0;
  iVar1 = FUN_ffff000090737554(_DAT_ffff00009083d080,0x70,&local_24);
  if (iVar1 == 0) {
    if ((char)local_24 < '\0') {
      uVar2 = 0x10;
    }
    else {
      uVar2 = 1;
    }
    iVar1 = FUN_ffff000090705730(uVar2,param_1);
  }
  else {
    FUN_ffff00009077e11c
              (s__s__d__Failed_to_regmap_read_MT6_ffff000090801d61,
               s__mt6379_charger_get_vbus_ffff0000907e7308,0x269);
  }
  return iVar1;
}


