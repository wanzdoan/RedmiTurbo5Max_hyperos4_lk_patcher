// ffff00009075316c  size=928  FUN_ffff00009075316c

/* WARNING: Globals starting with '_' overlap smaller symbols at the same address */

undefined8 FUN_ffff00009075316c(int param_1,long param_2)

{
  code *pcVar1;
  code *pcVar2;
  undefined1 *puVar3;
  undefined1 auStack_780 [12];
  int local_774;
  int local_5e8;
  
  if (param_2 == 0) {
    return 0xffffffff;
  }
  if (param_1 == 0x2d) {
    puVar3 = &DAT_ffff000090879570;
    _DAT_ffff000090879570 = FUN_ffff00009075350c;
    _DAT_ffff000090879590 = thunk_FUN_ffff00009077cbd8;
    _DAT_ffff000090879598 = FUN_ffff000090753158;
    _DAT_ffff000090879580 = FUN_ffff000090753580;
    FUN_ffff00009077eb88(auStack_780,0,0x750);
    (**(code **)(param_2 + 0x18))(auStack_780);
    if (local_774 == 1) {
      pcVar1 = FUN_ffff000090752ffc;
      pcVar2 = FUN_ffff000090752eb8;
      _DAT_ffff000090879600 = FUN_ffff000090752fa8;
      _DAT_ffff0000908795c0 = FUN_ffff000090752ef4;
      _DAT_ffff0000908795c8 = FUN_ffff000090752dcc;
      _DAT_ffff0000908795d0 = FUN_ffff000090752f54;
    }
    else if (local_774 == 2) {
      pcVar1 = FUN_ffff000090753018;
      pcVar2 = FUN_ffff000090752ecc;
      _DAT_ffff000090879600 = FUN_ffff000090752fc4;
      _DAT_ffff0000908795c0 = FUN_ffff000090752f14;
      _DAT_ffff0000908795c8 = FUN_ffff000090752df0;
      _DAT_ffff0000908795d0 = FUN_ffff000090752f70;
    }
    else {
      pcVar1 = FUN_ffff000090753034;
      pcVar2 = FUN_ffff000090752ee0;
      _DAT_ffff000090879600 = FUN_ffff000090752fe0;
      _DAT_ffff0000908795c0 = FUN_ffff000090752f34;
      _DAT_ffff0000908795c8 = FUN_ffff000090752e4c;
      _DAT_ffff0000908795d0 = FUN_ffff000090752f8c;
    }
    goto LAB_ffff000090753444;
  }
  if (param_1 == 0x2c) {
    puVar3 = &DAT_ffff000090879478;
    _DAT_ffff000090879478 = FUN_ffff00009075350c;
    _DAT_ffff000090879498 = thunk_FUN_ffff00009077cbd8;
    _DAT_ffff0000908794a0 = FUN_ffff000090753158;
    _DAT_ffff000090879488 = FUN_ffff000090753580;
    _DAT_ffff0000908794c8 = FUN_ffff000090752f14;
    _DAT_ffff0000908794d0 = FUN_ffff000090752df0;
    _DAT_ffff0000908794d8 = FUN_ffff000090752f70;
    _DAT_ffff000090879508 = FUN_ffff000090752fc4;
    _DAT_ffff000090879540 = FUN_ffff000090752e30;
    _DAT_ffff000090879548 = FUN_ffff000090752e94;
    _DAT_ffff000090879560 = FUN_ffff000090752ecc;
    _DAT_ffff000090879568 = FUN_ffff000090753018;
    goto code_r0xffff00009075344c;
  }
  if (param_1 != 0x2b) {
    return 0xffffffff;
  }
  puVar3 = &DAT_ffff000090879380;
  _DAT_ffff000090879380 = FUN_ffff00009075350c;
  _DAT_ffff0000908793a0 = thunk_FUN_ffff00009077cbd8;
  _DAT_ffff0000908793a8 = FUN_ffff000090753158;
  _DAT_ffff000090879390 = FUN_ffff000090753580;
  FUN_ffff00009077eb88(auStack_780,0,0x750);
  (**(code **)(param_2 + 0x18))(auStack_780);
  if (local_5e8 == 2) {
    if (local_774 == 2) {
      _DAT_ffff0000908793d8 = FUN_ffff000090752df0;
      _DAT_ffff0000908793e0 = FUN_ffff000090752f70;
      _DAT_ffff0000908793d0 = FUN_ffff000090752f14;
      _DAT_ffff000090879410 = FUN_ffff000090752fc4;
      pcVar1 = FUN_ffff000090753018;
      pcVar2 = FUN_ffff000090752ecc;
    }
    else {
      if (local_774 == 1) goto code_r0xffff0000907533c4;
      _DAT_ffff0000908793d8 = FUN_ffff000090752e4c;
      _DAT_ffff0000908793e0 = FUN_ffff000090752f8c;
      _DAT_ffff0000908793d0 = FUN_ffff000090752f34;
      _DAT_ffff000090879410 = FUN_ffff000090752fa8;
      pcVar1 = FUN_ffff000090752ffc;
      pcVar2 = FUN_ffff000090752ee0;
      puVar3 = &DAT_ffff000090879380;
    }
  }
  else {
    _DAT_ffff000090879448 = FUN_ffff000090752e14;
    _DAT_ffff000090879450 = FUN_ffff000090752e70;
code_r0xffff0000907533c4:
    _DAT_ffff000090879410 = FUN_ffff000090752fa8;
    _DAT_ffff0000908793e0 = FUN_ffff000090752f54;
    _DAT_ffff0000908793d8 = FUN_ffff000090752dcc;
    _DAT_ffff0000908793d0 = FUN_ffff000090752ef4;
    pcVar1 = FUN_ffff000090752ffc;
    pcVar2 = FUN_ffff000090752eb8;
    puVar3 = &DAT_ffff000090879380;
  }
LAB_ffff000090753444:
  *(code **)(puVar3 + 0xe8) = pcVar2;
  *(code **)(puVar3 + 0xf0) = pcVar1;
code_r0xffff00009075344c:
  *(code **)(puVar3 + 0xb8) = FUN_ffff000090753050;
  (**(code **)(param_2 + 8))(puVar3);
  return 0;
}


