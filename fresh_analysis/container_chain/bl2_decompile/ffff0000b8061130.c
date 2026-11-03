
undefined4 FUN_ffff0000b8061130(undefined8 param_1,undefined8 param_2,undefined8 param_3)

{
  undefined4 uVar1;
  
  FUN_ffff0000b804d070(&DAT_ffff0000b81b6dd8,0,0x100);
  DAT_ffff0000b81b6dd8 = 1;
  DAT_ffff0000b81b6dda = 1;
  FUN_ffff0000b806a384(&DAT_ffff0000b81b6dd8,0x100);
  FUN_ffff0000b806a384(param_1,0x100);
  FUN_ffff0000b806a384(param_2,0x100);
  uVar1 = FUN_ffff0000b8068668(param_1,&DAT_ffff0000b81b6dd8,param_2,param_3);
  FUN_ffff0000b806a384(param_2,0x100);
  FUN_ffff0000b806a384(param_3,0x100);
  return uVar1;
}


