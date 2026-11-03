// ffff00009075b638  size=144  FUN_ffff00009075b638

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075b638(undefined8 param_1,undefined8 param_2,long param_3)

{
  uint uVar1;
  undefined4 uVar2;
  long lVar3;
  undefined8 uVar4;
  uint local_24;
  
  local_24 = 0;
  _DAT_ffff00009087b6f4 = 0;
  if (param_3 == 0) {
    uVar4 = 0xffffffff;
  }
  else {
    uVar2 = FUN_ffff00009076da24(param_3,&DAT_ffff0000907d8022);
    lVar3 = FUN_ffff00009076df9c(param_3,uVar2,s_spr_ip_type_ffff0000907f6e0c,0);
    uVar4 = 0;
    if (lVar3 != 0) {
      FUN_ffff00009077eb20(&local_24,lVar3,4);
      uVar4 = 0;
      uVar1 = (local_24 & 0xff00ff00) >> 8 | (local_24 & 0xff00ff) << 8;
      _DAT_ffff00009087b6f4 = uVar1 >> 0x10 | uVar1 << 0x10;
    }
  }
  return uVar4;
}


