// ffff000090793d44  size=228  FUN_ffff000090793d44

undefined8 FUN_ffff000090793d44(long param_1,int param_2,undefined1 param_3,byte *param_4)

{
  undefined1 *puVar1;
  undefined8 uVar2;
  
  if (param_1 == 0) {
                    /* WARNING: Subroutine does not return */
    FUN_ffff00009077cd04
              (s_platform_mediatek_common_ufs_ufs_ffff0000907d6da1,0xfcc,&DAT_ffff0000907e04a3);
  }
  puVar1 = (undefined1 *)(param_1 + 0x1c0);
  FUN_ffff00009077eb88(puVar1,0,0x18);
  FUN_ffff00009077eb88(param_1 + 0x1e0,0,0x18);
  *(char *)(param_1 + 0x1c4) = (char)param_2;
  *(undefined1 *)(param_1 + 0x1c5) = param_3;
  *(undefined2 *)(param_1 + 0x1c6) = 0;
  if (param_2 - 6U < 3) {
    *puVar1 = 0x81;
LAB_ffff000090793db8:
    uVar2 = FUN_ffff000090793b8c(param_1,1,5000);
    if ((param_4 != (byte *)0x0) && ((int)uVar2 == 0)) {
      *param_4 = *(byte *)(param_1 + 0x1ef) & 1;
    }
  }
  else {
    if (param_2 == 5) {
      *puVar1 = 1;
      if (param_4 != (byte *)0x0) goto LAB_ffff000090793db8;
    }
    uVar2 = 0xffffffff;
  }
  return uVar2;
}


