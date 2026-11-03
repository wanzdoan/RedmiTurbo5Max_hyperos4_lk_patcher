// File offset entry: 0x66e14

undefined8 FUN_02066d14(void)

{
  undefined8 uVar1;
  
  if ((((iRam000000000011df40 == -0x3254cbee) && (iRam000000000011df44 == 0x40000)) &&
      (iRam000000000011df48 == 0)) &&
     ((((uint)(iRam000000000011df4c + iRam000000000011df58) < 0x40001 &&
       (uRam000000000011df50 < 0x80)) && (uRam000000000011df5c < 0x80)))) {
    uVar1 = 1;
  }
  else if (cRam00000000020e8100 == '\0') {
    uVar1 = 0;
  }
  else {
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20b9bee,0x20b58bc);
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20cc5e5,0x20b58bc,0x11df40,iRam000000000011df40,iRam000000000011df44);
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20beb45,0x20b58bc,iRam000000000011df48,iRam000000000011df4c,uRam000000000011df50)
    ;
    uRam000000000211be35 = 1;
    FUN_0208ada0(0x20cb46f,0x20b58bc,uRam000000000011df54,iRam000000000011df58,uRam000000000011df5c)
    ;
    uVar1 = 0;
    uRam000000000211be35 = 0;
    cRam00000000020e8100 = '\0';
  }
  return uVar1;
}


