// ffff0000907ae288  size=948  FUN_ffff0000907ae288

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

uint FUN_ffff0000907ae288
               (undefined8 param_1,undefined8 param_2,undefined8 param_3,undefined4 param_4,
               long param_5,undefined4 param_6)

{
  uint uVar1;
  int iVar2;
  long lVar3;
  long local_48;
  
  local_48 = param_5;
  FUN_ffff0000907b7f78();
  _DAT_ffff00009090c5cc = 0;
  DAT_ffff000090837ebc = 0x100;
  DAT_ffff000090837ec0 = 0x100;
  DAT_ffff000090837ec4 = 0x20;
  _DAT_ffff00009090c5d0 = 0;
  _DAT_ffff00009090c5d4 = 0;
  _DAT_ffff00009090c648 = (code *)PTR_FUN_ffff000090839b10;
  FUN_ffff00009077ee50(&DAT_ffff00009090c610,0,0x34);
  FUN_ffff00009077ee50(&DAT_ffff00009090c650,0,0x34);
  _DAT_ffff00009090c5c8 = _DAT_ffff00009090c5c8 + 1;
  FUN_ffff00009077ee50(0xffff00009090c684,0,0x464);
  _DAT_ffff00009090c884 = 0x200;
  _DAT_ffff00009090cacc = 0x30;
  _DAT_ffff00009090cad4 = 4;
  _DAT_ffff00009090ca90 = 0;
  _DAT_ffff00009090ca88 = 0x200;
  FUN_ffff00009077ee50(&DAT_ffff00009090cae8,0,0x17c);
  _DAT_ffff00009090cae8 = 0;
  _DAT_ffff00009090cb20 = 0x30;
  _DAT_ffff00009090cb54 = 0x30;
  _DAT_ffff00009090cb5c = 4;
  _DAT_ffff00009090cb64 = 4;
  _DAT_ffff00009090cbb0 = 4;
  FUN_ffff0000907b7fa4();
  FUN_ffff0000907b8018();
  lVar3 = FUN_ffff0000907b808c(0x4000);
  if (lVar3 == 0) {
    uVar1 = 7;
  }
  else {
    uVar1 = FUN_ffff0000907ae63c(param_2,param_3,param_4,&local_48);
    if (uVar1 != 0) goto LAB_ffff0000907ae47c;
    uVar1 = (*_DAT_ffff00009090c648)(param_1,lVar3,local_48,0x200);
    if (uVar1 != 0) goto LAB_ffff0000907ae47c;
    uVar1 = FUN_ffff0000907ad5bc(lVar3,0x200,&DAT_ffff00009090c650);
    if (uVar1 != 0) goto LAB_ffff0000907ae47c;
    if ((_DAT_ffff00009090c650 == 1) && (_DAT_ffff00009090c658 >> 1 == 0x1000000)) {
      if (_DAT_ffff00009090c654 + _DAT_ffff00009090c65c < 0x4001) {
        uVar1 = (*_DAT_ffff00009090c648)(param_1,lVar3,local_48);
        if (uVar1 != 0) goto LAB_ffff0000907ae47c;
        uVar1 = FUN_ffff0000907b1be4
                          (lVar3,_DAT_ffff00009090c65c + _DAT_ffff00009090c654,0xffff00009090c684);
        if (uVar1 != 0) goto LAB_ffff0000907ae47c;
        iVar2 = FUN_ffff0000907ad5ac(_DAT_ffff00009090c618);
        if ((iVar2 == 0x1000000) &&
           (iVar2 = FUN_ffff0000907aa6b4(_DAT_ffff00009090c618), iVar2 != 0)) {
          FUN_ffff00009077e11c(s_fail_to_set_MD_pubk_hash_ffff0000907f90b5);
        }
        local_48 = local_48 + (ulong)_DAT_ffff00009090c654 + (ulong)_DAT_ffff00009090c65c;
        uVar1 = (*_DAT_ffff00009090c648)(param_1,lVar3,local_48,0x200);
        if (uVar1 != 0) goto LAB_ffff0000907ae47c;
        uVar1 = FUN_ffff0000907ad5bc(lVar3,0x200,&DAT_ffff00009090c650);
        if (uVar1 != 0) goto LAB_ffff0000907ae47c;
        if (_DAT_ffff00009090c650 != 1) goto LAB_ffff0000907ae634;
        uVar1 = 0x10100d;
        if (_DAT_ffff00009090c658 != 0x2000002) goto LAB_ffff0000907ae47c;
        if (_DAT_ffff00009090c654 + _DAT_ffff00009090c65c < 0x4001) {
          uVar1 = (*_DAT_ffff00009090c648)(param_1,lVar3,local_48);
          if (uVar1 == 0) {
            uVar1 = FUN_ffff0000907b25b4
                              (lVar3,_DAT_ffff00009090c65c + _DAT_ffff00009090c654,
                               0xffff00009090c684,&DAT_ffff00009090cae8,param_6);
            if (uVar1 == 0) {
              FUN_ffff00009077e11c
                        (s___s___s_cert_chain_vfy_pass_ffff0000907fc90a,&DAT_ffff0000907f228a,
                         param_2);
              FUN_ffff0000907b8050();
              return 0;
            }
          }
          goto LAB_ffff0000907ae47c;
        }
      }
      uVar1 = 0x101019;
      goto LAB_ffff0000907ae47c;
    }
LAB_ffff0000907ae634:
    uVar1 = 0x100d;
  }
  uVar1 = uVar1 | 0x100000;
LAB_ffff0000907ae47c:
  FUN_ffff00009077e11c
            (s___s___s_cert_chain_vfy_fail__0x__ffff0000907fc927,&DAT_ffff0000907f228a,param_2,uVar1
            );
  FUN_ffff0000907b8050();
  FUN_ffff0000907af2b8();
  FUN_ffff0000907b7f90();
  return uVar1;
}


