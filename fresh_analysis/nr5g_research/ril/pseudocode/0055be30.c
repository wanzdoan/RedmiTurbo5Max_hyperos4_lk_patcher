
/* NetAgentService::runReaderLoop() */

void __thiscall NetAgentService::runReaderLoop(NetAgentService *this)

{
  uint uVar1;
  long lVar2;
  undefined8 uVar3;
  
  do {
    while( true ) {
      uVar3 = *(undefined8 *)(this + 0x18);
      uVar1 = isUserBuild(this);
      lVar2 = netagent_io_recv(uVar3,uVar1 & 1);
      if (lVar2 == 0) break;
      enqueueReqInfo(this,lVar2,0);
    }
    mtkLogE("NetAgentService","[%s] recv urc fail","runReaderLoop");
  } while( true );
}


