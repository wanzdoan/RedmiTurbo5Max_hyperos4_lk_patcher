// File offset entry: 0x3ebc0

undefined4 FUN_ffff0000b803ebc0(undefined8 param_1,undefined8 param_2)

{
  long lVar1;
  undefined4 uVar2;
  
  if ((DAT_ffff0000b812727c & 1) == 0) {
    FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
    DAT_ffff0000b812727c = 1;
  }
  if (DAT_ffff0000b8128850 == 0) {
    lVar1 = FUN_ffff0000b803d4d4(param_1,param_2);
    if ((DAT_ffff0000b812727c & 1) == 0) {
      FUN_ffff0000b804cbe4(&DAT_ffff0000b8128840,0,0x38);
      DAT_ffff0000b812727c = 1;
    }
    uVar2 = 3;
    DAT_ffff0000b8128850 = lVar1;
    if (lVar1 != 0) {
      uVar2 = 0;
    }
  }
  else {
    uVar2 = 0;
  }
  return uVar2;
}


